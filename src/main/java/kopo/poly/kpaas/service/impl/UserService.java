package kopo.poly.kpaas.service.impl;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.kpaas.dto.MailDTO;
import kopo.poly.kpaas.dto.UserDTO;
import kopo.poly.kpaas.mapper.UserMapper;
import kopo.poly.kpaas.service.IUserService;
import kopo.poly.kpaas.util.CmmUtil;
import kopo.poly.kpaas.util.CoolSmsUtil;
import kopo.poly.kpaas.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserMapper userMapper;
    private final JavaMailSender mailSender;

    /** 로그인 */
    private final UserMapper userMapper;

    private final CoolSmsUtil coolSmsUtil;

    @Value("${spring.mail.username}")
    private String fromMail;

    @Override
    public UserDTO login(UserDTO pDTO) throws Exception {
        log.info("🟢 login() 호출: {}", pDTO);
    public int doSendMail(MailDTO pDTO) {
        log.info("doSendMail start!");
        int res = 1;

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // ✅ Mapper에서 email + password 조건으로 바로 조회
        UserDTO rDTO = userMapper.getLogin(pDTO);
            helper.setTo(pDTO.getToMail());
            helper.setFrom(fromMail);
            helper.setSubject(pDTO.getTitle());
            helper.setText(pDTO.getContents(), true);

        if (rDTO != null) {
            log.info("✅ 로그인 성공: {}", rDTO.getUserId());
        } else {
            log.warn("❌ 로그인 실패 (이메일 또는 비밀번호 불일치)");
            mailSender.send(message);

            log.info("메일 발송 성공!");
        } catch (Exception e) {
            res = 0;
            log.error("메일 발송 실패: {}", e.getMessage(), e);
        } finally {
            log.info("doSendMail end!");
        }

        return rDTO;
        return res;
    }

    /** 마이페이지 프로필 조회 */
    @Override
    public UserDTO getUserProfile(UserDTO pDTO) throws Exception {
        return userMapper.getUserProfile(pDTO);
    public UserDTO getUserByEmail(String email) throws Exception {
        log.info("getUserByEmail start!");

        UserDTO pDto = new UserDTO();
        pDto.setEmail(email);

        UserDTO rDto = userMapper.getUserByEmail(pDto);

        log.info("getUserByEmail end!");
        return rDto;
    }


    /** 회원 탈퇴 */
    @Override
    public int deleteUser(UserDTO pDTO) throws Exception {
        return userMapper.deleteUser(pDTO);
    public int updatePassword(UserDTO pDto) throws Exception {
        log.info("updatePassword start!");

        // ✅ 암호화
        String hashPw = EncryptUtil.encHashSHA256(pDto.getPassword());
        pDto.setPassword(hashPw);

        int res = userMapper.updatePassword(pDto);

        log.info("updatePassword result: {}", res);
        log.info("updatePassword end!");
        return res;
    }


    /** 비밀번호 변경 */
    @Override
    public int updatePassword(UserDTO pDTO) throws Exception {
        pDTO.setPassword(EncryptUtil.encHashSHA256(pDTO.getPassword()));
        return userMapper.updatePasswordByEmail(pDTO);
    public UserDTO getUserByNameAndPhone(String name, String tel) throws Exception {
        UserDTO pDto = new UserDTO();
        pDto.setName(name);
        pDto.setTel(tel);

        return userMapper.getUserByNameAndPhone(pDto);
    }

    /** 이메일로 사용자 조회 */
    @Override
    public UserDTO getUserByEmail(UserDTO pDTO) throws Exception {
        return userMapper.getUserByEmail(pDTO);
    public String maskEmail(String email) {
        int idx = email.indexOf("@");
        if (idx > 3) {
            return email.substring(0, 4) + "****" + email.substring(idx);
        } else {
            return "****" + email.substring(idx);
        }
    }

    /** 휴대폰으로 사용자 조회 */
    // ✅ 이메일 찾기용 인증코드 생성 + 세션 저장 + SMS 발송
    @Override
    public UserDTO getUserByPhone(UserDTO pDTO) throws Exception {
        return userMapper.getUserByPhone(pDTO);
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

        // CoolSMS 발송
        coolSmsUtil.sendVerificationCode(tel, code);

        log.info("✅ 이메일 찾기 인증코드 생성 및 전송 완료: {}", code);
        log.info("sendFindEmailCode end!");
    }
}
