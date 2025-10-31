package kopo.poly.kpaas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CountryDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer countryId;
    private String countryName;
    private String countryCode;
}

