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
    private Long contractId;
    private String riskChartData;
    private Integer totalRiskLevel;  // 🔹 int
    private String translatedText;
    private String createdAt;
}