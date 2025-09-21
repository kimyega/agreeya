package kopo.poly.kpaas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractDTO {

    private Integer contractId;
    private Integer userId;
    private Integer countryId;
    private String originalFileUrl;
    private String ocrText;
    private String createdAt;
}
