package kopo.poly.kpaas.service.impl;

import jakarta.mail.internet.MimeMessage;
import kopo.poly.kpaas.dto.MailDTO;
import kopo.poly.kpaas.dto.UserDTO;
import kopo.poly.kpaas.mapper.IUserMapper;
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


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {


    private final IUserMapper userMapper;

    private final CoolSmsUtil coolSmsUtil;



    @Override
    public UserDTO getUserByEmail(UserDTO pDTO) throws Exception {
        log.info("getUserByEmail start!");
        pDTO.setEmail(CmmUtil.nvl(pDTO.getEmail())); // null 방지

        UserDTO rDTO = userMapper.getUserByEmail(pDTO);

        log.info("getUserByEmail end!");
        return rDTO;
    }

    /**
     * 비밀번호 업데이트
     */
    @Override
    public int updatePassword(UserDTO pDto) throws Exception {
        log.info("updatePassword start!");

        String rawPw = CmmUtil.nvl(pDto.getPassword());
        String hashPw = EncryptUtil.encHashSHA256(rawPw); // SHA256 암호화
        pDto.setPassword(hashPw);

        int res = userMapper.updatePassword(pDto);

        log.info("updatePassword result: {}", res);
        log.info("updatePassword end!");
        return res;
    }

    /**
     * 이름+전화번호로 사용자 조회
     */
    @Override
    public UserDTO getUserByNameAndPhone(UserDTO pDTO) throws Exception {
        log.info("getUserByNameAndPhone start!");
        pDTO.setName(CmmUtil.nvl(pDTO.getName()));
        pDTO.setTel(CmmUtil.nvl(pDTO.getTel()));

        UserDTO rDTO = userMapper.getUserByNameAndPhone(pDTO);

        log.info("getUserByNameAndPhone end!");
        return rDTO;
    }


    /**
     * 이메일 찾기용 인증코드 생성 + SMS 발송 (세션은 Controller에서 처리)
     */
    @Override
    public String sendFindEmailCode(UserDTO pDTO) {
        log.info("sendFindEmailCode start!");

        String tel = CmmUtil.nvl(pDTO.getTel());

        String code = String.format("%06d", (int) (Math.random() * 1000000));
        coolSmsUtil.sendVerificationCode(tel, code);

        log.info("✅ 이메일 찾기 인증코드 발송 완료");
        log.info("sendFindEmailCode end!");
        return code;
    }

}



