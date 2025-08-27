package kopo.poly.kpaas.dto;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AnalysisSummaryDto {
    private Integer contractId;
    private String  riskChartData;   // JSON 문자열
    private Integer totalRiskLevel;
    private String  translatedText;
    private LocalDateTime createdAt; // 조인해서 표시
}
