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
        private int res;      // 처리 결과 (1=성공, 0=실패)
        private String msg;   // 메시지


}
