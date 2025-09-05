package kopo.poly.kpaas.service.impl;

import jakarta.mail.internet.MimeMessage;
import kopo.poly.kpaas.dto.MailDTO;
import kopo.poly.kpaas.dto.UserDTO;
import kopo.poly.kpaas.mapper.UserMapper;
import kopo.poly.kpaas.service.IUserService;
import kopo.poly.kpaas.util.CmmUtil;
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

    private final JavaMailSender mailSender;

    private final UserMapper userMapper;

    @Value("${spring.mail.username}")
    private String fromMail;

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

    @Override
    public UserDTO getUserByEmail(String email) throws Exception {
        log.info("getUserByEmail start!");

        UserDTO pDto = new UserDTO();
        pDto.setEmail(email);

        UserDTO rDto = userMapper.getUserByEmail(pDto);

        log.info("getUserByEmail end!");
        return rDto;
    }

    @Override
    public UserDTO getUserByPhone(String phone) throws Exception {
        return null;
    }

    @Override
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

}


