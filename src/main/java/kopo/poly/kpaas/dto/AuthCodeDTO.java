// AuthCodeDTO.java
package kopo.poly.kpaas.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AuthCodeDTO {

    private String email;
    private String authCode;
    private LocalDateTime limitedTime; // 인증번호 생성 시각
}
