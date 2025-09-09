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



    /**
     * 로그인 처리
     */
    @Override
    public UserDTO getUserlogin(String email, String password) throws Exception {
        log.info("login start!");

        UserDTO pDTO = new UserDTO();
        pDTO.setEmail(CmmUtil.nvl(email));
        pDTO.setPassword(CmmUtil.nvl(password));

        // DB에서 사용자 조회
        UserDTO rDTO = userMapper.getUserLogin(pDTO);

        if (rDTO == null) {
            log.warn("❌ 사용자 없음 - email={}", email);
            return null;
        }

        // DB 저장된 비밀번호
        String dbPw = CmmUtil.nvl(rDTO.getPassword());
        String rawPw = CmmUtil.nvl(password);
        String hashPw = EncryptUtil.encHashSHA256(rawPw);

        // 평문 또는 해시 비교
        if (dbPw.equals(rawPw) || dbPw.equals(hashPw)) {
            log.info("✅ 로그인 성공 - userId={}, name={}", rDTO.getUserId(), rDTO.getName());
            return rDTO;
        }

        log.warn("❌ 비밀번호 불일치 - email={}", email);
        return null;
    }

    /**
     * 프로필 조회 (userId 기준)
     */
    @Override
    public UserDTO getUserProfile(String userId) throws Exception {
        log.info("getUserProfile start!");

        UserDTO pDTO = new UserDTO();
        pDTO.setUserId(CmmUtil.nvl(userId));

        UserDTO rDTO = userMapper.getUserProfile(pDTO);

        log.info("getUserProfile end! result={}", rDTO);
        return rDTO;
    }

    /**
     * 휴대폰으로 사용자 조회
     */
    @Override
    public UserDTO getUserByPhone(UserDTO pDTO) throws Exception {
        log.info("getUserByPhone start!");
        UserDTO rDTO = userMapper.getUserByPhone(pDTO);
        log.info("getUserByPhone end!");
        return rDTO;
    }

    /**
     * 회원 탈퇴
     */
    @Override
    public int deleteUser(String userId) throws Exception {
        log.info("deleteUser start!");

        UserDTO pDTO = new UserDTO();
        pDTO.setUserId(CmmUtil.nvl(userId));

        int res = userMapper.deleteUser(pDTO);

        log.info("deleteUser result={}", res);
        log.info("deleteUser end!");
        return res;
    }




    /**
     * 이메일로 사용자 조회
     */
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



