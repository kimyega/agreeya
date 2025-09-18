package kopo.poly.kpaas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LawDTO {
    private int lawId;
    private int countryId;
    private String title;
    private String articleNumber;
    private String content;
    private String updatedAt;
    private String lawVector;
}
