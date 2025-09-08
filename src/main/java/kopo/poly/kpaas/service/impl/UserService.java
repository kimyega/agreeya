package kopo.poly.kpaas.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.kpaas.dto.MailDTO;
import kopo.poly.kpaas.dto.UserDTO;
import kopo.poly.kpaas.mapper.UserMapper;
import kopo.poly.kpaas.service.IUserService;
import kopo.poly.kpaas.util.CoolSmsUtil;
import kopo.poly.kpaas.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserMapper userMapper;
    private final JavaMailSender mailSender;
    private final CoolSmsUtil coolSmsUtil;

    @Value("${spring.mail.username}")
    private String fromMail;

    /** 로그인 */
    @Override
    public UserDTO login(UserDTO pDTO) throws Exception {
        log.info("🟢 login() 호출: {}", pDTO);

        String rawPw = pDTO.getPassword(); // 사용자가 입력한 비밀번호 (평문)
        String hashPw = EncryptUtil.encHashSHA256(rawPw); // 해시된 버전도 생성

        // 이메일로 사용자 조회
        UserDTO dbUser = userMapper.getUserByEmail(pDTO);

        if (dbUser == null) {
            log.warn("❌ 로그인 실패 - 사용자 없음");
            return null;
        }

        // ✅ DB에 저장된 비번이 평문일 수도 있고, 해시일 수도 있음 → 둘 다 비교
        if (dbUser.getPassword().equals(rawPw) || dbUser.getPassword().equals(hashPw)) {
            log.info("✅ 로그인 성공 userId={}", dbUser.getUserId());
            return dbUser;
        }

        log.warn("❌ 로그인 실패 - 비밀번호 불일치");
        return null;
    }

    /** 마이페이지 프로필 조회 */
    @Override
    public UserDTO getUserProfile(UserDTO pDTO) throws Exception {
        return userMapper.getUserProfile(pDTO);
    }

    /** 회원 탈퇴 */
    @Override
    public int deleteUser(UserDTO pDTO) throws Exception {
        return userMapper.deleteUser(pDTO);
    }

    /** 비밀번호 변경 (이메일 기준) */
    @Override
    public int updatePassword(UserDTO pDTO) throws Exception {
        String hashPw = EncryptUtil.encHashSHA256(pDTO.getPassword());
        pDTO.setPassword(hashPw);
        return userMapper.updatePasswordByEmail(pDTO);
    }

    /** 이메일로 사용자 조회 */
    @Override
    public UserDTO getUserByEmail(UserDTO pDTO) throws Exception {
        return userMapper.getUserByEmail(pDTO);
    }

    /** 휴대폰으로 사용자 조회 */
    @Override
    public UserDTO getUserByPhone(UserDTO pDTO) throws Exception {
        return userMapper.getUserByPhone(pDTO);
    }

    /** 메일 발송 */
    @Override
    public int doSendMail(MailDTO pDTO) {
        log.info("doSendMail start!");
        int res = 1;

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(pDTO.getToMail());
            helper.setFrom(fromMail);
            helper.setSubject(pDTO.getTitle());
            helper.setText(pDTO.getContents(), true);

            mailSender.send(message);
            log.info("메일 발송 성공!");
        } catch (Exception e) {
            res = 0;
            log.error("메일 발송 실패: {}", e.getMessage(), e);
        } finally {
            log.info("doSendMail end!");
        }

        return res;
    }

    /** 이메일 찾기용 인증번호 발송 */
    @Override
    public void sendFindEmailCode(HttpServletRequest request, String name, String tel) {
        log.info("sendFindEmailCode start!");

        // 6자리 인증번호 생성
        String code = String.format("%06d", (int) (Math.random() * 1000000));

        // 세션 저장
        HttpSession session = request.getSession();
        session.setAttribute("findEmailCode", code);
        session.setAttribute("findEmailTel", tel);
        session.setAttribute("findEmailName", name);
        session.setAttribute("findEmailExpire", System.currentTimeMillis() + 5 * 60 * 1000);

        // SMS 발송
        coolSmsUtil.sendVerificationCode(tel, code);

        log.info("✅ 이메일 찾기 인증코드 생성 및 전송 완료: {}", code);
        log.info("sendFindEmailCode end!");
    }
}
