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
    private String contractId;
    private String userId;
    private String countryId;
    private String originalFileUrl;
    private String ocrText;
    private String createdAt;


    private Integer riskCount;   // 위험 요소 건수
    private String riskLevel;    // 낮음/보통/높음
}
