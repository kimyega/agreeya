package kopo.poly.kpaas.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import kopo.poly.kpaas.dto.ContractDTO;
import kopo.poly.kpaas.mapper.ICaseMapper;
import kopo.poly.kpaas.service.ICaseService;
import kopo.poly.kpaas.util.OpenAiEmbeddingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CaseService implements ICaseService {

    private final ICaseMapper caseMapper;
    private final GptService gptService;  // GPT 호출 서비스

    private final ObjectMapper objectMapper;

    @Override
    public List<ContractDTO> getSimilarCases(ContractDTO pDTO) throws Exception {
        log.info("▶ getSimilarCases 시작 contractId={}", pDTO.getContractId());

        // 1️⃣ 다른 계약서 리스트
        List<ContractDTO> others = caseMapper.getOtherContracts(pDTO);
        if (others == null || others.isEmpty()) {
            log.warn("❌ 다른 계약서가 존재하지 않습니다.");
            return Collections.emptyList();
        }
        log.info("🔹 다른 계약서 개수: {}", others.size());

        // 2️⃣ InMemoryEmbeddingStore
        InMemoryEmbeddingStore<Document> store = new InMemoryEmbeddingStore<>();
        int addedCount = 0;
        for (ContractDTO c : others) {
            if (c.getContractVector() == null || c.getOcrText() == null) {
                log.warn("⚠️ 스킵: contractId={}, vector={}, ocrText={}", c.getContractId(), c.getContractVector(), c.getOcrText());
                continue;
            }
            float[] vec = objectMapper.readValue(c.getContractVector(), float[].class);
            store.add(new Embedding(vec), Document.from(c.getOcrText()));
            addedCount++;
        }
        log.info("🔹 InMemoryEmbeddingStore에 저장된 계약서 수: {}", addedCount);

        // 3️⃣ 기준 계약서
        ContractDTO rDTO = caseMapper.getContractById(pDTO);
        if (rDTO == null || rDTO.getContractVector() == null) {
            log.warn("❌ 기준 계약서 벡터가 없습니다.");
            return Collections.emptyList();
        }
        float[] baseVec = objectMapper.readValue(rDTO.getContractVector(), float[].class);
        Embedding baseEmbedding = new Embedding(baseVec);
        log.info("🔹 기준 계약서 벡터 길이: {}", baseVec.length);

        // 4️⃣ 유사한 상위 3개 검색
        List<EmbeddingMatch<Document>> topMatches = store.findRelevant(baseEmbedding, 3);
        log.info("🔹 검색된 유사 사례 수: {}", topMatches.size());
        for (EmbeddingMatch<Document> m : topMatches) {
            log.info("    ✅ 매치 텍스트 샘플: {}", m.embedded().text().substring(0, Math.min(50, m.embedded().text().length())));
        }

        if (topMatches.isEmpty()) {
            log.warn("❌ 유사 사례가 없습니다.");
            return Collections.emptyList();
        }

        // 5️⃣ GPT 분석
        List<ContractDTO> topCases = topMatches.stream()
                .map(match -> {
                    ContractDTO dto = new ContractDTO();
                    dto.setOcrText(match.embedded().text());
                    return dto;
                })
                .toList();

        for (ContractDTO caseDto : topCases) {
            String prompt = """
                    다음 계약서를 분석해서 JSON으로 출력해줘.
                    {
                      "title": "...",
                      "summary": "...",
                      "category": "..."
                    }
                    계약서:
                    %s
                    """.formatted(caseDto.getOcrText());

            String rawJson = gptService.generateText(prompt);
            String cleaned = cleanJson(rawJson);

            try {
                JsonNode root = objectMapper.readTree(cleaned);
                caseDto.setTitle(root.path("title").asText("제목 없음"));
                caseDto.setSummary(root.path("summary").asText("요약 없음"));
                caseDto.setCategory(root.path("category").asText("미분류"));
                log.info("✅ GPT 파싱 성공: title={}, category={}", caseDto.getTitle(), caseDto.getCategory());
            } catch (Exception e) {
                log.error("❌ GPT 응답 파싱 실패: {}", cleaned, e);
                caseDto.setTitle("제목 생성 실패");
                caseDto.setSummary("요약 생성 실패");
                caseDto.setCategory("미분류");
            }
        }

        log.info("📌 상위 {}개 사례 GPT 처리 완료", topCases.size());
        return topCases;
    }

    // GPT 응답에서 ```json, ``` 제거
    private String cleanJson(String gptResponse) {
        if (gptResponse == null) return "";
        return gptResponse
                .replaceAll("```json", "")
                .replaceAll("```", "")
                .trim();
    }
}
