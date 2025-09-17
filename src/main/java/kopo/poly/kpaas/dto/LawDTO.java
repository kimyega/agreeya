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
    private Integer id;
    private Integer countryId;       // countries 테이블 FK
    private String countryCode;      // 선택적으로 표시
    private String title;
    private String articleNumber;
    private String content;
    private double[] lawVector;
    private String lawVectorJson;
    private double similarity;       // 검색 시 사용
}

