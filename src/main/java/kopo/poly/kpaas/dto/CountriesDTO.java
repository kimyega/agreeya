package kopo.poly.kpaas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountriesDTO {
    private Integer countryId;
    private String countryName;   // e.g., "European Union"
    private String countryCode;   // e.g., "EU"
}

