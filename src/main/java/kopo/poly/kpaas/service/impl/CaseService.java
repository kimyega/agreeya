package kopo.poly.kpaas.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final OpenAiEmbeddingUtil embeddingUtil;
    private final GptService gptService;  // GPT 호출 서비스

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<ContractDTO> getSimilarCases(ContractDTO pDTO) throws Exception {
        log.info("▶ getSimilarCases 시작 contractId={}", pDTO.getContractId());

        // 1. 기준 계약서 텍스트 가져오기
        String baseText = caseMapper.getContractTextById(pDTO.getContractId());
        if (baseText == null || baseText.isBlank()) {
            return Collections.emptyList();
        }
        List<Float> baseVector = embeddingUtil.createEmbedding(baseText);

        // 2. 다른 계약서 목록 가져오기
        List<ContractDTO> others = caseMapper.getOtherContracts(pDTO.getContractId());
        if (others == null || others.isEmpty()) {
            return Collections.emptyList();
        }

        // 3. 유사도 계산 + GPT 요약/제목/분류
        for (ContractDTO other : others) {
            if (other.getOcrText() == null || other.getOcrText().isBlank()) continue;

            List<Float> vec = embeddingUtil.createEmbedding(other.getOcrText());
            double sim = cosineSimilarity(baseVector, vec);
            other.setSimilarity(sim);

            // GPT 응답 받기
            String prompt = "다음 계약서를 분석해서 JSON으로 출력해줘.\n" +
                    "{\n" +
                    "  \"title\": \"...\",   // 계약서 제목 (한 줄)\n" +
                    "  \"summary\": \"...\", // 계약서 요약 (2~3문장)\n" +
                    "  \"category\": \"...\" // 근로시간, 계약 해지, 임금, 복리후생 중 하나\n" +
                    "}\n계약서:\n" + other.getOcrText();

            String rawJson = gptService.generateText(prompt);
            String cleaned = cleanJson(rawJson);

            try {
                JsonNode root = objectMapper.readTree(cleaned);
                other.setTitle(root.path("title").asText("제목 없음"));
                other.setSummary(root.path("summary").asText("요약 없음"));
                other.setCategory(root.path("category").asText("미분류"));
                log.info("✅ GPT 파싱 성공: title={}, category={}", other.getTitle(), other.getCategory());
            } catch (Exception e) {
                log.error("❌ GPT 응답 파싱 실패: {}", cleaned, e);
                // 실패 시 fallback 값
                other.setTitle("제목 생성 실패");
                other.setSummary("요약 생성 실패");
                other.setCategory("미분류");
            }
        }

        // 4. 유사도 순 정렬 후 상위 5개
        return others.stream()
                .sorted(Comparator.comparingDouble(ContractDTO::getSimilarity).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    // GPT 응답에서 ```json, ``` 제거
    private String cleanJson(String gptResponse) {
        if (gptResponse == null) return "";
        return gptResponse
                .replaceAll("```json", "")
                .replaceAll("```", "")
                .trim();
    }

    private double cosineSimilarity(List<Float> v1, List<Float> v2) {
        if (v1 == null || v2 == null || v1.size() != v2.size()) return 0.0;
        double dot = 0.0, norm1 = 0.0, norm2 = 0.0;
        for (int i = 0; i < v1.size(); i++) {
            dot += v1.get(i) * v2.get(i);
            norm1 += Math.pow(v1.get(i), 2);
            norm2 += Math.pow(v2.get(i), 2);
        }
        return dot / (Math.sqrt(norm1) * Math.sqrt(norm2) + 1e-10);
    }
}
