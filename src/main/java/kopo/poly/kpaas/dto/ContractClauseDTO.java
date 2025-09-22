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
    private String clauseId;
    private String contractId;
    private String clauseText;
    private String riskScore;
    private String riskType;
    private String aiComment;
}