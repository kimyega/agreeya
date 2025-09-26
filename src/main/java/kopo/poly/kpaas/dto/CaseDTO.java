package kopo.poly.kpaas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Getter + Setter + toString + equals + hashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaseDTO {
    private String contractId;
    private String countryId;
    private String lawId;
    private String title;
    private String articleNumber;
    private String content;
    private String riskType;
    private double score;
    private String clauseText;
    private String lawVector;  // ✅ 법령 벡터 추가
}