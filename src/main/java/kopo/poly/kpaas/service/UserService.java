package kopo.poly.kpaas.service;

import kopo.poly.kpaas.dto.EmailVerifyDTO;
import kopo.poly.kpaas.dto.UserDTO;
import kopo.poly.kpaas.mapper.UserMapper;
import kopo.poly.kpaas.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper userMapper;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailAddress;

    /** 인증코드 유효시간(분) */
    private static final int EXPIRE_MINUTES = 5;

    /** 사용자열거(Email Enumeration) 방지 — 외부 응답에서 존재여부 숨김 */
    private static final boolean ANTI_ENUMERATION = true;


    /* ========== 기존 컨트롤러 호환(RESET_PASSWORD 고정) ========== */

    /** 비밀번호 재설정: 이메일 확인 후 인증코드 전송 */
    public String checkEmailAndSendCode(String email) throws Exception {
        String norm = normalize(email);
        log.info("📩 [비번재설정] 인증코드 전송 시작: {}", norm);

        UserDTO rDTO = userMapper.getUserByEmail(norm);
        if (rDTO == null) {
            log.warn("❌ [비번재설정] 존재하지 않는 이메일 요청: {}", norm);
            if (ANTI_ENUMERATION) {
                // 존재여부를 응답에 드러내지 않음 (메일 미발송)
                log.info("🛡  Anti-enumeration: 성공처럼 응답 처리, 실제 메일 미발송");
                return norm;
            } else {
                throw new Exception("에러가 발생 하였습니다");
            }
        }

        sendVerifyCode(norm, EmailVerifyDTO.RESET_PASSWORD);
        log.info("📨 [비번재설정] 인증코드 전송 완료: {}", norm);
        return rDTO.getEmail();
    }

    /** 비밀번호 재설정: 인증코드 검증 */
    public boolean verifyCode(String email, String inputCode) {
        return verifyCode(email, EmailVerifyDTO.RESET_PASSWORD, inputCode);
    }


    /* ========== 범용(목적 전달) ========== */

    /**
     * 인증코드 전송 (SIGNUP / RESET_PASSWORD)
     * - RESET_PASSWORD: 가입된 이메일만 허용
     * - SIGNUP: 아직 가입되지 않은 이메일만 허용
     * - 해시/만료 계산은 SQL에서 처리
     */
    public void sendVerifyCode(String email, String purpose) {
        String norm = normalize(email);
        log.info("📩 [코드전송] 시작: email={}, purpose={}", norm, purpose);

        // 항상 users에서 조회 → 존재/주인 확인
        UserDTO rDTO = userMapper.getUserByEmail(norm);

        if (EmailVerifyDTO.RESET_PASSWORD.equals(purpose)) {
            if (rDTO == null) {
                log.warn("❌ [코드전송] 비번재설정 - 미가입 이메일: {}", norm);
                if (ANTI_ENUMERATION) {
                    log.info("🛡  Anti-enumeration: 성공처럼 응답, 메일 미발송");
                    return;
                } else {
                    throw new IllegalArgumentException("이메일이 존재하지 않습니다");
                }
            }
        } else if (EmailVerifyDTO.SIGNUP.equals(purpose)) {
            if (rDTO != null) {
                log.warn("❌ [코드전송] 회원가입 - 이미 가입된 이메일: {}", norm);
                if (ANTI_ENUMERATION) {
                    log.info("🛡  Anti-enumeration: 성공처럼 응답, 메일 미발송");
                    return;
                } else {
                    throw new IllegalArgumentException("이미 가입된 이메일입니다");
                }
            }
        } else {
            log.warn("❌ [코드전송] 알 수 없는 purpose: {}", purpose);
            throw new IllegalArgumentException("허용되지 않은 요청입니다.");
        }

        // 6자리 코드 생성 (평문은 메일에만 사용, DB는 SQL에서 해시 저장)
        String code = String.valueOf((int)(Math.random() * 900000) + 100000);
        log.debug("✅ 생성된 인증번호: {}", code);

        // SQL이 SHA2 해시/만료를 처리
        userMapper.upsertEmailVerifySqlSide(norm, purpose, code, EXPIRE_MINUTES);

        // 메일 수신자 결정
        String to = (EmailVerifyDTO.RESET_PASSWORD.equals(purpose) && rDTO != null)
                ? rDTO.getEmail()
                : norm;

        // 메일 발송
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(mailAddress);
        msg.setTo(to);
        msg.setSubject("[안심계약] 인증번호 안내");
        msg.setText("""
                안녕하세요. 안심계약입니다.

                인증번호: %s

                %d분 이내에 입력해 주세요.
                """.formatted(code, EXPIRE_MINUTES));
        mailSender.send(msg);

        log.info("📨 [코드전송] 완료: email={}, purpose={}, to={}", norm, purpose, to);
    }

    /**
     * 인증코드 검증 (성공 시 1회성 소모)
     * - SQL이 해시 비교 + 유효시간 체크 + 삭제까지 처리
     * - 영향 행 수가 1이면 성공, 0이면 실패(없음/만료/불일치)
     */
    public boolean verifyCode(String email, String purpose, String inputCode) {
        String norm = normalize(email);
        int affected = userMapper.verifyAndConsumeSqlSide(norm, purpose, inputCode);
        if (affected == 1) {
            log.info("✅ [코드검증] 성공: email={}, purpose={}", norm, purpose);
            return true;
        }
        log.warn("❌ [코드검증] 실패(없음/만료/불일치): email={}, purpose={}", norm, purpose);
        throw new IllegalArgumentException("인증 코드가 유효하지 않거나 만료되었습니다.");
    }

    /** 비밀번호 변경 — BCrypt 해시 저장 */
    public void updatePassword(String email, String newPassword) {
        String norm = normalize(email);
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

    /** 이메일 공백 제거 + 소문자화 (조회 일관성 확보) */
    private String normalize(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}
