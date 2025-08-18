package kopo.poly.kpaas.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import kopo.poly.kpaas.dto.AuthCodeDTO;
import kopo.poly.kpaas.dto.UserDTO;
import kopo.poly.kpaas.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper userMapper;

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailAddress;


    /**
     * 입력된 이메일이 DB에 존재하는지 확인하고,
     * 존재하면 인증번호를 생성하여 이메일로 전송
     */

    public String checkEmailAndSendCode(String email) throws Exception {


        log.info("📩 이메일 존재 확인 및 인증번호 전송 시작: {}", email);

        // 1. DB에서 이메일로 사용자 정보 조회
        UserDTO rDTO = userMapper.getUserByEmail(email);

        // 2. 사용자 없으면 false 반환
        if (rDTO == null) {
            log.warn("❌ 존재하지 않는 이메일: {}", email);
            throw new Exception("에러가 발생 하였습니다");
        }

        // 3. 인증번호 생성 (랜덤 6자리 숫자)
        int code = (int) (Math.random() * 900000) + 100000; // 예: 123456 ~ 999999
        String authCode = String.valueOf(code);

        log.info("✅ 생성된 인증번호: {}", authCode);

        // 인증번호 저장
        AuthCodeDTO dto = new AuthCodeDTO();
        dto.setEmail(email);
        dto.setAuthCode(authCode);
        dto.setLimitedTime(LocalDateTime.now()); // 생성 시간 저장

        // TODO: 인증번호를 세션, DB, Redis 등에 저장해야 검증 가능
        // 지금은 생략. 다음 단계에서 인증번호 검증 구현 예정

        // 4. 메일 전송 준비
        SimpleMailMessage message = new SimpleMailMessage();
        //보내는 사람꺼
        message.setFrom(mailAddress);

        // 받는 사람: 테스트용 이메일 주소
        message.setTo(rDTO.getUserEmail());

        // 제목
        message.setSubject("[안심계약] 비밀번호 재설정 인증번호 안내");

        // 본문 (간단한 텍스트)
        message.setText("""
                안녕하세요. 안심계약입니다.

                비밀번호 재설정을 위한 인증번호는 아래와 같습니다.

                📌 인증번호: %s

                5분 이내에 입력해주세요.
                """.formatted(authCode));

        // 5. 메일 전송

        mailSender.send(message);
        log.info("📨 인증번호 메일 전송 성공!");

        userMapper.updateEmailCodeByEmail(dto);

        return rDTO.getUserEmail(); // 성공
    }


    public boolean verifyCode(String email, String inputCode) throws Exception {

        // 1. DB에서 이메일로 사용자 정보 조회
        UserDTO rDTO = userMapper.getUserByEmail(email);

        // 2. 사용자 없으면 false 반환
        if (rDTO == null) {
            log.warn("❌ 존재하지 않는 이메일: {}", email);
            throw new Exception("이메일이 존재하지 않습니다");
        }

        if (!rDTO.getAuthCode().equals(inputCode)) {
            log.warn("❌ 존재하지 않는 인증 코드: {}", inputCode);
            throw new Exception("인증 코드가 유효 하지 않습니다");
        }

        LocalDateTime now = LocalDateTime.now();

        // 3. 시간 차이 계산
        Duration duration = Duration.between(rDTO.getLimitedTime(), now);
        long minutes = duration.toMinutes(); // 분 단위 차이

        if (minutes > 5) {
            log.warn("❌ 만료된 인증 코드: {}", inputCode);
            throw new Exception("인증 코드가 만료 되었습니다");
        }

        return true;

    }

    public void updatePassword(String email, String newPassword) throws Exception {
        /*
         * 비밀번호 암호화
         * 기본 salt 값 : 10
         */
        String hashedPw = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        UserDTO dto = new UserDTO();
        dto.setUserEmail(email);
        dto.setUserPassword(hashedPw);
        userMapper.updatePasswordByEmail(dto);
    }
}

