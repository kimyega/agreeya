package kopo.poly.kpaas.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LawDTO {
    private String lawId;
    private int countryId;
    private String title;
    private String articleNumber;
    private String content;
    private String updatedAt;
    private String lawVector;
}
