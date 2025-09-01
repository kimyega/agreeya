package kopo.poly.kpaas.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class EmailVerifyDTO {

    // contact_type 상수 (문자열로 유지: Enum 안 써도 됨)
    public static final String TYPE_EMAIL = "EMAIL";
    public static final String TYPE_PHONE = "PHONE";

    // purpose 상수
    public static final String SIGNUP = "SIGNUP";
    public static final String RESET_PASSWORD = "RESET_PASSWORD";
    public static final String FIND_EMAIL_BY_PHONE = "FIND_EMAIL_BY_PHONE";

    // === 새 스키마 필드 ===
    private String contactType;    // EMAIL | PHONE
    private String contactValue;   // 이메일 주소 or 전화번호
    private String purpose;        // 위 상수 중 하나

    private String codeHash;       // SHA-256 hex 64자
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
}
