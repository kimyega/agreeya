package kopo.poly.kpaas.service.impl;

import jakarta.mail.internet.MimeMessage;
import kopo.poly.kpaas.dto.MailDTO;
import kopo.poly.kpaas.dto.UserDTO;
import kopo.poly.kpaas.service.IEmailService;
import kopo.poly.kpaas.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromMail;

    /**
     * 메일 발송
     */
    @Override
    public int doSendMail(MailDTO pDTO) {
        log.info("doSendMail start!");
        int res = 1;

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(CmmUtil.nvl(pDTO.getToMail())); // null 방지
            helper.setFrom(fromMail);
            helper.setSubject(CmmUtil.nvl(pDTO.getTitle()));
            helper.setText(CmmUtil.nvl(pDTO.getContents()), true);

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

    /**
     * 이메일 마스킹
     */
    @Override
    public UserDTO maskEmail(UserDTO pDTO) {
        String safeEmail = CmmUtil.nvl(pDTO.getEmail());
        int idx = safeEmail.indexOf("@");

        String masked;
        if (idx > 3) {
            masked = safeEmail.substring(0, 4) + "****" + safeEmail.substring(idx);
        } else {
            masked = "****" + safeEmail.substring(idx);
        }

        pDTO.setEmail(masked); // DTO에 세팅
        return pDTO;
    }
}