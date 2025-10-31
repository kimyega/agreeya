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
public class CountriesDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer countryId;
    private String countryName;   // e.g., "European Union"
    private String countryCode;   // e.g., "EU"
}

