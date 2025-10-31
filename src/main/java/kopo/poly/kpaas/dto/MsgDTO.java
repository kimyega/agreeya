package kopo.poly.kpaas.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class MsgDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int result;
    private String msg;
    private String name;
}