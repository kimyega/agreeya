package kopo.poly.kpaas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractDTO {

    // 기본 계약서 정보
    private String contractId;
    private String userId;
    private String countryId;
    private String originalFileUrl;
    private String ocrText;
    private String createdAt;

    // 유사도 계산 관련
    private Double similarity;   // 계약서끼리 비교
    private Double score;        // 법령/조항 비교

    // AI 분석 결과 (추천 리스트용)
    private String title;     // 계약서 제목
    private String summary;   // 요약
    private String category;  // 유형 (예: 근로시간, 계약해지, 임금, 복리후생 등)



    private Integer riskCount;   // 위험 요소 건수
    private String riskLevel;    // 낮음/보통/높음
}
