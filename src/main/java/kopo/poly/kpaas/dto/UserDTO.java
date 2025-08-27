package kopo.poly.kpaas.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserDTO {

        private String userId;
        private String name;
        private String email;
        private String nickname;
        private String password;
        private String birthDate;
        private String tel;
        private String isForeigner;

}
