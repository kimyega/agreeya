package kopo.poly.kpaas.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

// ✅ LangChain4j 관련 import
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModelName;

import kopo.poly.kpaas.dto.ContractDTO;
import kopo.poly.kpaas.dto.ContractClauseDTO;
import kopo.poly.kpaas.dto.ContractAnalysisSummaryDTO;
import kopo.poly.kpaas.dto.LawDTO;
import kopo.poly.kpaas.mapper.IAnalysisMapper;
import kopo.poly.kpaas.service.IAnalysisService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AnalysisService implements IAnalysisService {

    private final IAnalysisMapper analysisMapper;
    private final GptService gptService;
    private final ObjectMapper mapper = new ObjectMapper();

    private final OpenAiEmbeddingModel embeddingModel;

    // ✅ 생성자 주입 (규칙 준수)
    public AnalysisService(IAnalysisMapper analysisMapper,
                           GptService gptService,
                           @Value("${openai.api.key}") String apiKey) {
        this.analysisMapper = analysisMapper;
        this.gptService = gptService;

        // OpenAI 임베딩 모델 초기화
        this.embeddingModel = OpenAiEmbeddingModel.builder()
                .apiKey(apiKey)
                .modelName(OpenAiEmbeddingModelName.TEXT_EMBEDDING_3_SMALL)
                .build();
    }

    @Override
    public void analyzeContract(ContractDTO pDTO) throws Exception {

        String contractId = pDTO.getContractId();
        String countryId = pDTO.getCountryId();
        String userId = pDTO.getUserId();

        log.info("➡️ 분석 시작: userId={}, contractId={}, countryId={}", userId, contractId, countryId);

        // 1. 계약서 OCR 텍스트 조회 (단건)
        ContractDTO rDTO = analysisMapper.getContractById(pDTO);
        if (rDTO == null || rDTO.getOcrText() == null) {
            throw new Exception("계약서 OCR 텍스트가 없습니다. contractId=" + contractId);
        }
        String contractText = rDTO.getOcrText();

        // 2. 국가별 법령 조회 (여러 건)
        List<LawDTO> lawList = analysisMapper.getLawsByCountryId(
                LawDTO.builder().countryId(Integer.parseInt(countryId)).build()
        );
        if (lawList == null || lawList.isEmpty()) {
            throw new Exception("해당 국가 법령이 없습니다. countryId=" + countryId);
        }

        // ============================================================
        // ✅ (랭체인 시작) 모든 법령 내용을 chunk로 나누고 임베딩 저장
        // ============================================================
        InMemoryEmbeddingStore<Document> store = new InMemoryEmbeddingStore<>();
        for (LawDTO law : lawList) {
            if (law.getContent() == null) continue;
            String[] lawChunks = law.getContent().split("\\n\\n+"); // 빈 줄 기준 분리
            for (String chunk : lawChunks) {
                if (!chunk.isBlank()) {
                    Embedding emb = embeddingModel.embed(chunk).content();
                    store.add(emb, Document.from(chunk));
                }
            }
        }

        // ✅ 계약서 OCR 텍스트 임베딩 → 유사한 법령 Top 50 검색 TOP의 갯수 가 더 넘어가면 api 토큰 수 한계 넘어감
        Embedding contractEmbedding = embeddingModel.embed(contractText).content();
        List<EmbeddingMatch<Document>> matches = store.findRelevant(contractEmbedding, 30);

        StringBuilder similarLaws = new StringBuilder();
        for (EmbeddingMatch<Document> match : matches) {
            similarLaws.append(match.embedded().text()).append("\n\n");
        }
        log.info("🔎 유사한 법령 추출 완료, {}개 선택", matches.size());
        // ============================================================
        // ✅ (랭체인 끝)
        // ============================================================

        // 3. GPT 프롬프트 생성
        String prompt = """
        [계약서 전체 내용]
        %s

        [관련 법령 발췌]
        %s

        이 계약서를 조항 단위로 분석하고, 동시에 전체 요약도 작성해주세요.
        반드시 JSON 형식으로만 응답하세요:

        {
          "clauses": [
            {
              "clause_text": "조항 원문",
              "risk_score": 0~100 숫자,
              "risk_type": "임금/근로시간/휴가/기타 등",
              "ai_comment": "상세 분석"
            }
          ],
          "summary": {
            "riskChartData": {"임금":개수, "근로시간":개수, "휴가":개수, "기타":개수},
            "totalRiskLevel": 0~100 숫자,
            "translatedText": "계약서 요약본"
          }
        }
        """.formatted(contractText, similarLaws.toString());

        String gptResponse = gptService.generateText(prompt);
        log.debug("🤖 GPT 전체 응답: {}", gptResponse);

        // ✅ 전처리: GPT가 줄 수 있는 ```json ... ``` 제거
        String cleaned = gptResponse
                .replace("```json", "")
                .replace("```", "")
                .trim();

        // ✅ JSON 파싱
        JsonNode root = mapper.readTree(cleaned);

        if (root.has("clauses")) {
            for (JsonNode c : root.get("clauses")) {
                ContractClauseDTO cDTO = ContractClauseDTO.builder()
                        .contractId(Integer.parseInt(contractId))
                        .clauseText(c.path("clause_text").asText(""))
                        .riskScore(c.path("risk_score").asInt(0))
                        .riskType(c.path("risk_type").asText("기타"))
                        .aiComment(c.path("ai_comment").asText(""))
                        .build();
                analysisMapper.insertClauseResult(cDTO);
            }
            log.info("✅ 조항별 분석 {}건 저장 완료", root.get("clauses").size());
        }

        if (root.has("summary")) {
            JsonNode summary = root.get("summary");
            ContractAnalysisSummaryDTO sDTO = ContractAnalysisSummaryDTO.builder()
                    .contractId(Integer.parseInt(contractId))
                    .riskChartData(summary.path("riskChartData").toString())
                    .totalRiskLevel(summary.path("totalRiskLevel").asInt(0))
                    .translatedText(summary.path("translatedText").asText(""))
                    .build();
            analysisMapper.insertSummaryResult(sDTO);
            log.info("✅ 요약 결과 저장 완료");
        }

        log.info("📌 계약서 분석 종료");
    }
}
