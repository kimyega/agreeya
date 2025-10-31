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
public class NegotiationDTO implements Serializable {
  private static final long serialVersionUID = 1L;
  private Integer negotiationId;
  private Integer userId;
  private Integer contractId;
  private String scenarioInput;
  private String aiConversation;
  private String summaryText;
  private String createdAt;

  // 데이터 전달용 필드

  private String situation;
  private String country;
  private String position;
  private String goal;
}
