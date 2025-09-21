package kopo.poly.kpaas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractClauseDTO {
    private Integer clauseId;
    private Long contractId;
    private String clauseText;
    private Integer riskScore;   // 🔹 int
    private String riskType;
    private String aiComment;
}