package kopo.poly.kpaas.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractResultDTO implements Serializable {
  private static final long serialVersionUID = 1L;
  private ContractAnalysisSummaryDTO summary;
  private List<ContractClauseDTO> clauses;
}
