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
public class LawDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer lawId;
    private Integer countryId;
    private String title;
    private String articleNumber;
    private String content;
    private String updatedAt;
    private String lawVector;
}
