package kopo.poly.kpaas.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class EmailVerifyDTO {

    /** 인증 목적 상수 (오타 방지) */
    public static final String SIGNUP = "SIGNUP";
    public static final String RESET_PASSWORD = "RESET_PASSWORD";

    /** 이메일 (PK part) */
    private String email;

    /** 인증 목적 (PK part): SIGNUP / RESET_PASSWORD */
    private String purpose;

    /** 인증코드의 SHA-256 해시(hex 64자) — 평문 저장 금지 */
    private String codeHash;

    /** 발송 시점 기준 만료 시각 (NOW + 5분 등) */
    private LocalDateTime expiresAt;

    /** 생성 시각(DB default CURRENT_TIMESTAMP) */
    private LocalDateTime createdAt;
}
