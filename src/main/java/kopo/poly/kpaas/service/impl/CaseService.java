package kopo.poly.kpaas.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.kpaas.dto.CaseDTO;
import kopo.poly.kpaas.dto.LawDTO;
import kopo.poly.kpaas.mapper.ICaseMapper;
import kopo.poly.kpaas.mapper.ILawMapper;
import kopo.poly.kpaas.service.ICaseService;
import kopo.poly.kpaas.util.OpenAiEmbeddingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CaseService implements ICaseService {

    private final ICaseMapper caseMapper;
    private final ILawMapper lawMapper;
    private final OpenAiEmbeddingUtil embeddingUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<CaseDTO> getSimilarCases(CaseDTO pDTO) throws Exception {
        log.info("▶ getSimilarCases 시작 contractId={} countryId={}",
                pDTO.getContractId(), pDTO.getCountryId());

        // 1. 계약 조항 가져오기
        List<CaseDTO> clauses = caseMapper.getClausesByContractId(pDTO.getContractId());
        if (clauses == null || clauses.isEmpty()) {
            log.warn("❌ 계약 조항 없음 contractId={}", pDTO.getContractId());
            return Collections.emptyList();
        }

        // 2. 국가 ID 확인
        String countryId = pDTO.getCountryId();
        if (countryId == null || countryId.isEmpty()) {
            log.warn("❌ countryId 없음 → 유사사례 조회 불가");
            return Collections.emptyList();
        }

        // 3. 국가별 법령 불러오기 (LawDTO로 조회)
        List<LawDTO> laws = lawMapper.getLawsByCountryId(countryId);
        if (laws == null || laws.isEmpty()) {
            log.warn("❌ 선택 국가 [{}] 법령 없음", countryId);
            return Collections.emptyList();
        }

        // 4. 계약 조항 → 벡터 변환 (예시: 첫 번째 조항만 사용)
        String clauseText = clauses.get(0).getClauseText();
        if (clauseText == null || clauseText.isBlank()) {
            log.warn("❌ contractId={} 조항 텍스트가 비어있음", pDTO.getContractId());
            return Collections.emptyList();
        }
        List<Float> clauseVector = embeddingUtil.createEmbedding(clauseText);

        // 5. 코사인 유사도 계산
        List<CaseDTO> results = new ArrayList<>();
        for (LawDTO law : laws) {
            List<Float> lawVector = parseVector(law.getLawVector());
            if (lawVector.isEmpty()) continue;

            double similarity = cosineSimilarity(clauseVector, lawVector);

            CaseDTO dto = new CaseDTO();
            dto.setContractId(pDTO.getContractId());   // 요청받은 contractId
            dto.setCountryId(countryId);              // 선택된 국가
            dto.setLawId(law.getLawId());             // 법령 ID
            dto.setTitle(law.getTitle());
            dto.setArticleNumber(law.getArticleNumber());
            dto.setContent(law.getContent());
            dto.setRiskType(pDTO.getRiskType());
            dto.setScore(similarity);

            results.add(dto);

        }

        // 6. 유사도 높은 TOP 5 반환
        return results.stream()
                .sorted(Comparator.comparingDouble(CaseDTO::getScore).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    /**
     * JSON 문자열 → 벡터(List<Float>) 변환
     */
    private List<Float> parseVector(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<Float>>() {});
        } catch (Exception e) {
            log.error("❌ law_vector 파싱 실패", e);
            return Collections.emptyList();
        }
    }

    /**
     * 두 벡터 간 코사인 유사도 계산
     */
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
