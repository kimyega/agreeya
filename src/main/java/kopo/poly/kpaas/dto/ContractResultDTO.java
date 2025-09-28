package kopo.poly.kpaas.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractResultDTO {
  private ContractAnalysisSummaryDTO summary;
  private List<ContractClauseDTO> clauses;
}
