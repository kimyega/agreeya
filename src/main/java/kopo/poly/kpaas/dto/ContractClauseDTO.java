package kopo.poly.kpaas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractClauseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer clauseId;
    private Integer contractId;
    private String clauseText;
    private Integer riskScore;
    private String riskType;
    private String aiComment;
}