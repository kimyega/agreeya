package kopo.poly.kpaas.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

        private String userId;
        private String name;
        private String email;
        private String nickname;
        private String password;
        private String birthDate;
        private String tel;
        private String isForeigner;
        private String createdAt;  // ✅ 가입일

}
