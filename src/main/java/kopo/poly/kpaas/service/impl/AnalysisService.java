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

    // ✅ 생성자 주입 (규칙 준수, @Autowired 불필요)
    public AnalysisService(IAnalysisMapper analysisMapper,
                           GptService gptService,
                           @Value("${openai.api.key}") String apiKey) {
        this.analysisMapper = analysisMapper;
        this.gptService = gptService;

        // OpenAI 임베딩 모델 초기화
        this.embeddingModel = OpenAiEmbeddingModel.builder()
                .apiKey(apiKey) // application.yaml 값 자동 주입
                .modelName(OpenAiEmbeddingModelName.TEXT_EMBEDDING_3_SMALL)
                .build();
    }

    @Override
    public void analyzeContract(ContractDTO pDTO) throws Exception {

        Long contractId = pDTO.getContractId();
        Long countryId = pDTO.getCountryId();
        String userId = String.valueOf(pDTO.getUserId());

        log.info("➡️ 분석 시작: userId={}, contractId={}, countryId={}", userId, contractId, countryId);

        // 1. 계약서 OCR 텍스트 조회
        ContractDTO rDTO = analysisMapper.getContractById(pDTO);
        if (rDTO == null || rDTO.getOcrText() == null) {
            throw new Exception("계약서 OCR 텍스트가 없습니다. contractId=" + contractId);
        }
        String contractText = rDTO.getOcrText();

        // 2. 국가별 법령 조회
        LawDTO lawDTO = analysisMapper.getLawByCountryId(
                LawDTO.builder().countryId(countryId).build()
        );
        if (lawDTO == null || lawDTO.getContent() == null) {
            throw new Exception("해당 국가 법령이 없습니다. countryId=" + countryId);
        }
        String lawText = lawDTO.getContent();

        // ============================================================
        // ✅ (랭체인 시작) 법령 내용을 chunk로 나누고 임베딩 저장
        // ============================================================
        InMemoryEmbeddingStore<Document> store = new InMemoryEmbeddingStore<>();
        String[] lawChunks = lawText.split("\\n\\n+"); // 빈 줄 기준 분리
        for (String chunk : lawChunks) {
            if (!chunk.isBlank()) {
                Embedding emb = embeddingModel.embed(chunk).content();
                store.add(emb, Document.from(chunk));
            }
        }

        // ✅ 계약서 OCR 텍스트 임베딩 → 유사한 법령 Top 10 검색
        Embedding contractEmbedding = embeddingModel.embed(contractText).content();
        List<EmbeddingMatch<Document>> matches = store.findRelevant(contractEmbedding, 10);

        StringBuilder similarLaws = new StringBuilder();
        for (EmbeddingMatch<Document> match : matches) {
            similarLaws.append(match.embedded().text()).append("\n\n");
        }
        log.info("🔎 유사한 법령 추출 완료, {}개 선택", matches.size());
        // ============================================================
        // ✅ (랭체인 끝)
        // ============================================================

        // 3. GPT 프롬프트 (계약서 + 유사 법령 포함)
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

        // 4. JSON 파싱 및 DB 저장
        JsonNode root = mapper.readTree(gptResponse);

        if (root.has("clauses")) {
            for (JsonNode c : root.get("clauses")) {
                ContractClauseDTO cDTO = ContractClauseDTO.builder()
                        .contractId(contractId)
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
                    .contractId(contractId)
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
