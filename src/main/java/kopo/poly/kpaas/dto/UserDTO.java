package kopo.poly.kpaas.dto;

import lombok.*;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        private String userId;
        private String name;
        private String email;
        private String nickname;
        private String password;
        private String birthDate;
        private String tel;
        private int isForeigner;
        private String createdAt;  // ✅ 가입일

        private String existsYn;
        private int authNumber;


}
