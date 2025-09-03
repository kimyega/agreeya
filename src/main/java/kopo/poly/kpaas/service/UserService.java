package kopo.poly.kpaas.service;

import kopo.poly.kpaas.dto.UserDTO;
import kopo.poly.kpaas.mapper.UserMapper;
//import kopo.poly.kpaas.util.CoolSmsUtil;
import kopo.poly.kpaas.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service // 팀 방침: 서비스 하나(UserService). 필요시 이름 바꾸면 알려줘.
@RequiredArgsConstructor
public class UserService {
    public UserDTO login(String email, String password) {
        String norm = normalizeEmail(email);
        UserDTO rDTO = userMapper.getUserByEmail(norm);
        if (rDTO == null) {
            log.warn("❌ 로그인 실패 - 사용자 없음");
            return null;
        }

        String dbPw = rDTO.getPassword();
        if (dbPw != null &&
                (dbPw.equals(password) || dbPw.equals(EncryptUtil.encHashSHA256(password)))) {
            log.info("✅ 로그인 성공 userId={}", rDTO.getUserId());
            return rDTO;
        }

        log.warn("❌ 로그인 실패 - 비밀번호 불일치");
        return null;
    }

    /** 마이페이지 프로필 조회 */
    public UserDTO getUserProfile(String userId) {
        return userMapper.getUserProfile(userId);
    }

    /** 회원 탈퇴 */
    public int deleteUser(String userId) throws Exception {
        return userMapper.deleteUser(Integer.parseInt(userId));
    }

    private final UserMapper userMapper;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailAddress;

//    private final CoolSmsUtil coolSmsUtil;

    /**
     * 인증코드 유효시간(분)
     */
    private static final int EXPIRE_MINUTES = 5;
    /**
     * 존재여부 숨김(Email Enumeration 방지)
     */
    private static final boolean ANTI_ENUMERATION = true;

    /**
     * 새 스키마 contact_type: 이메일 인증은 EMAIL 고정
     */
    private static final String CONTACT_TYPE_EMAIL = "EMAIL";
    /**
     * purpose: 비밀번호 재설정
     */
    private static final String PURPOSE_RESET_PASSWORD = "RESET_PASSWORD";

    /**
     * 이메일 공백 제거 + 소문자(쿼리 LOWER 비교와 일관 유지)
     */
    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    // =============== 호환 메서드들(기존 컨트롤러 시그니처 유지) ===============

    /**
     * (호환) 기존 컨트롤러: 이메일 확인 후 인증코드 전송 + 존재여부 비노출
     */
    public String checkEmailAndSendCode(String email) throws Exception {
        String norm = normalizeEmail(email);
        log.info("📩 [비번재설정] 인증코드 전송 시작: {}", norm);

        UserDTO rDTO = userMapper.getUserByEmail(norm);
        if (rDTO == null) {
            log.warn("❌ [비번재설정] 존재하지 않는 이메일 요청: {}", norm);
            if (ANTI_ENUMERATION) {
                // 존재여부를 응답에 드러내지 않음 (메일 미발송)
                log.info("🛡 Anti-enumeration: 성공처럼 응답 처리, 실제 메일 미발송");
                return norm;
            } else {
                throw new Exception("에러가 발생 하였습니다");
            }
        }

        // 기존 호출과 호환: 내부에서 RESET_PASSWORD만 전송
        sendVerifyCode(norm, PURPOSE_RESET_PASSWORD);

        log.info("📨 [비번재설정] 인증코드 전송 완료: {}", norm);
        return rDTO.getEmail();
    }

    /**
     * (호환) 기존: 목적 없이 검증 → RESET_PASSWORD 고정
     */
    public boolean verifyCode(String email, String inputCode) {
        return verifyCode(email, PURPOSE_RESET_PASSWORD, inputCode);
    }

    // ===================== 비밀번호 재설정(핵심 동작) =====================

    /**
     * 인증코드 전송 (RESET_PASSWORD만 사용)
     * - 가입된 이메일만 허용 (미가입이면 Anti-enumeration 정책으로 조용히 성공)
     * - 해시/만료 계산은 SQL에서 처리 (upsert)
     */
    public void sendVerifyCode(String email, String purpose) {
        // ※ 주의: 지금 단계에선 RESET_PASSWORD만 허용
        if (!PURPOSE_RESET_PASSWORD.equals(purpose)) {
            throw new IllegalArgumentException("허용되지 않은 purpose입니다.");
        }

        String norm = normalizeEmail(email);
        log.info("📩 [코드전송] 시작: email={}, purpose={}", norm, purpose);

        // 가입여부 체크
        UserDTO rDTO = userMapper.getUserByEmail(norm);
        if (rDTO == null) {
            log.warn("❌ [코드전송] 비번재설정 - 미가입 이메일: {}", norm);
            if (ANTI_ENUMERATION) {
                log.info("🛡 Anti-enumeration: 성공처럼 응답, 메일 미발송");
                return;
            } else {
                throw new IllegalArgumentException("이메일이 존재하지 않습니다");
            }
        }

        // 6자리 코드 생성 (평문은 메일에만 사용, DB는 SQL에서 해시 저장)
        String code = String.valueOf((int) (Math.random() * 900000) + 100000);
        log.debug("✅ 생성된 인증번호: {}", code);

        // ✅ 업서트 저장: (contact_type, contact_value, purpose, plainCode, expireMin)

        userMapper.upsertEmailVerifySqlSide(
                CONTACT_TYPE_EMAIL, // contact_type = 'EMAIL'
                norm,               // contact_value = 정규화 이메일
                purpose,            // purpose = 'RESET_PASSWORD'
                code,               // 평문 코드 (SQL에서 SHA2 처리)
                EXPIRE_MINUTES      // 분
        );

        // 메일 발송
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(mailAddress);
        msg.setTo(norm);
        msg.setSubject("[안심계약] 비밀번호 재설정 인증번호");
        msg.setText("""
                안녕하세요. 안심계약입니다.
                
                비밀번호 재설정 인증번호: %s
                
                %d분 이내에 입력해 주세요.
                """.formatted(code, EXPIRE_MINUTES));
        mailSender.send(msg);

        log.info("📨 [코드전송] 완료: email={}, purpose={}", norm, purpose);
    }

    /**
     * 인증코드 검증 (성공 시 1회성 소모)
     * - SQL이 해시 비교 + 유효시간 체크 + 삭제까지 처리
     * - 영향 행 수가 1이면 성공, 0이면 실패(없음/만료/불일치)
     */
    public boolean verifyCode(String email, String purpose, String inputCode) {
        if (!PURPOSE_RESET_PASSWORD.equals(purpose)) {
            throw new IllegalArgumentException("허용되지 않은 purpose입니다.");
        }

        String norm = normalizeEmail(email);

        // ✅ 반드시 지금 있는 매퍼 메서드만 사용
        int affected = userMapper.verifyAndConsumeSqlSide(
                CONTACT_TYPE_EMAIL, // contact_type
                norm,               // contact_value
                purpose,            // purpose
                inputCode           // 평문 입력(해시는 SQL에서 SHA2 비교)
        );

        if (affected == 1) {
            log.info("✅ [코드검증] 성공: email={}, purpose={}", norm, purpose);
            return true;
        }
        log.warn("❌ [코드검증] 실패(없음/만료/불일치): email={}, purpose={}", norm, purpose);
        throw new IllegalArgumentException("인증 코드가 유효하지 않거나 만료되었습니다.");
    }

    /**
     * 비밀번호 변경 — 팀 표준(EncryptUtil: SHA-256)
     */
    public void updatePassword(String email, String newPassword) {
        String norm = normalizeEmail(email);
        UserDTO rDTO = userMapper.getUserByEmail(norm);
        if (rDTO == null) {
            log.warn("❌ [비번변경] 존재하지 않는 이메일: {}", norm);
            throw new IllegalArgumentException("요청을 처리할 수 없습니다.");
        }

        String hashed = EncryptUtil.encHashSHA256(newPassword);

        UserDTO dto = new UserDTO();
        dto.setEmail(rDTO.getEmail());
        dto.setPassword(hashed);
        userMapper.updatePasswordByEmail(dto);

        log.info("🔐 [비번변경] 완료: {}", rDTO.getEmail());
    }

    //이메일 찾기 + 전화 번호

    private String normalizePhone(String raw) {
        if (raw == null) return "";
        String digits = raw.replaceAll("\\D", "");
        if (digits.startsWith("10")) digits = "0" + digits; // '10123...' 입력 보정
        return digits;
    }

    public void sendFindEmailCode(String rawPhone) {
        String phone = normalizePhone(rawPhone);

        var user = userMapper.getUserByPhone(phone);
        if (user == null) {
            throw new IllegalArgumentException("등록되지 않은 번호입니다.");
        }

        String code = String.format("%06d", ThreadLocalRandom.current().nextInt(1_000_000));
        userMapper.upsertEmailVerifySqlSide("PHONE", phone, "FIND_EMAIL_BY_PHONE", code, 5);

        String msg = "[안심계약] 이메일 찾기 인증번호: " + code + " (5분 이내 입력)\n타인 노출에 주의하세요.";
//        coolSmsUtil.sendVerificationCode(phone, code); // CoolSmsUtil 주입되어 있어야 함
    }

    public String verifyFindEmailCode(String rawPhone, String inputCode) {
        String phone = normalizePhone(rawPhone);

        int consumed = userMapper.verifyAndConsumeSqlSide("PHONE", phone, "FIND_EMAIL_BY_PHONE", inputCode);
        if (consumed <= 0) {
            throw new IllegalArgumentException("인증번호가 올바르지 않거나 만료되었습니다.");
        }

        var user = userMapper.getUserByPhone(phone);
        if (user == null || user.getEmail() == null) {
            throw new IllegalStateException("사용자 정보를 찾을 수 없습니다.");
        }
        return maskEmail(user.getEmail());
    }

    private String maskEmail(String email) {
        int at = email.indexOf('@');
        if (at <= 0) return email;

        String local = email.substring(0, at);
        String domain = email.substring(at + 1); // 도메인 그대로 유지

        // 앞 4글자만 남기고 나머지 마스킹
        String localMasked;
        if (local.length() <= 4) {
            // 4글자 이하인 경우: 첫 글자만 보이고 나머지 마스킹
            localMasked = local.charAt(0) + "*".repeat(local.length() - 1);
        } else {
            localMasked = local.substring(0, 4) + "*".repeat(local.length() - 4);
        }

        return localMasked + "@" + domain;
    }
}