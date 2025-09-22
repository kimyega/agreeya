package kopo.poly.kpaas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractAnalysisSummaryDTO {
    private Integer summaryId;
    private Integer contractId;
    private String riskChartData;
    private Integer totalRiskLevel;
    private String translatedText;
    private String createdAt;
}