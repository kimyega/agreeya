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
public class DraftDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String draftKr;
    private String draftJp;
    private String draftEn;

}
