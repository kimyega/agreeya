package kopo.poly.kpaas.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserDTO {

        private String userId;
        private String userEmail;
        private String userTel;
        private String userName;
        private String userPassword;
        private LocalDateTime registrationDate;
        private LocalDateTime updateDate;
        private String userBirthDate;
        private String userNickName;
        private String userCountry;
        private LocalDateTime limitedTime;
        private String authCode;



}
