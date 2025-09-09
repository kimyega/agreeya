package kopo.poly.kpaas.service.impl;

import kopo.poly.kpaas.dto.MailDTO;
import kopo.poly.kpaas.dto.UserDTO;
import kopo.poly.kpaas.mapper.IUserMapper;
import kopo.poly.kpaas.service.IEmailService;
import kopo.poly.kpaas.service.IUserService;
import kopo.poly.kpaas.util.CmmUtil;
import kopo.poly.kpaas.util.CoolSmsUtil;
import kopo.poly.kpaas.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {


    private final IUserMapper userMapper;

    private final CoolSmsUtil coolSmsUtil;

    private final IEmailService emailService;



    /**
     * 로그인 처리
     */
    @Override
    public UserDTO getUserLogin(UserDTO pDTO) throws Exception {
        log.info("getUserLogin start!");

        // null 방지
        pDTO.setEmail(CmmUtil.nvl(pDTO.getEmail()));
        pDTO.setPassword(CmmUtil.nvl(pDTO.getPassword()));

        // DB에서 사용자 조회
        UserDTO rDTO = userMapper.getUserLogin(pDTO);

        if (rDTO == null) {
            log.warn("❌ 사용자 없음 - email={}", pDTO.getEmail());
            return null;
        }

        // DB 저장된 비밀번호
        String dbPw = CmmUtil.nvl(rDTO.getPassword());
        String inputPw = CmmUtil.nvl(pDTO.getPassword());
        String hashPw = EncryptUtil.encHashSHA256(inputPw);

        // 평문 또는 해시 비교
        if (dbPw.equals(inputPw) || dbPw.equals(hashPw)) {
            log.info("✅ 로그인 성공 - userId={}, name={}", rDTO.getUserId(), rDTO.getName());
            return rDTO;
        }

        log.warn("❌ 비밀번호 불일치 - email={}", pDTO.getEmail());
        return null;
    }

    /**
     * 프로필 조회 (userId 기준)
     */
    @Override
    public UserDTO getUserProfile(UserDTO pDTO) throws Exception {
        log.info("getUserProfile start!");

        pDTO.setUserId(CmmUtil.nvl(pDTO.getUserId())); // null 방지
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

        pDTO.setTel(CmmUtil.nvl(pDTO.getTel())); // null 방지
        UserDTO rDTO = userMapper.getUserByPhone(pDTO.getTel());

        log.info("getUserByPhone end!");
        return rDTO;
    }

    /**
     * 회원 탈퇴
     */
    @Override
    public int deleteUser(UserDTO pDTO) throws Exception {
        log.info("deleteUser start!");

        // null 방지
        pDTO.setUserId(CmmUtil.nvl(pDTO.getUserId()));

        // MyBatis delete → 삭제된 행 수(int) 반환
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
        // 조회 조건 암호화
        pDTO.setEmail(EncryptUtil.encAES128BCBC(CmmUtil.nvl(pDTO.getEmail())));

        UserDTO rDTO = userMapper.getUserByEmail(pDTO);

        // 조회 결과 복호화
        if (rDTO != null && rDTO.getEmail() != null) {
            rDTO.setEmail(EncryptUtil.decAES128BCBC(rDTO.getEmail()));
        }

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

        // 이메일 조건 암호화 (세션에서 가져온 이메일이 평문일 가능성 있음)
        if (pDto.getEmail() != null) {
            pDto.setEmail(EncryptUtil.encAES128BCBC(pDto.getEmail()));
        }

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

        // 조건 암호화
        pDTO.setTel(EncryptUtil.encAES128BCBC(CmmUtil.nvl(pDTO.getTel())));

        UserDTO rDTO = userMapper.getUserByNameAndPhone(pDTO);

        // 결과 복호화
        if (rDTO != null) {
            if (rDTO.getTel() != null) {
                rDTO.setTel(EncryptUtil.decAES128BCBC(rDTO.getTel()));
            }
            if (rDTO.getEmail() != null) {
                rDTO.setEmail(EncryptUtil.decAES128BCBC(rDTO.getEmail()));
            }
        }

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

    @Override
    public int insertUser(UserDTO pDTO) throws Exception {

        log.info("{}.insertUserInfo Start!", this.getClass().getName());

        int res = 0;

        int success = userMapper.insertUser(pDTO);

        if (success > 0) {
            res = 1;

            MailDTO mDto = new MailDTO();

            mDto.setToMail(EncryptUtil.encAES128BCBC(CmmUtil.nvl(pDTO.getEmail())));
            mDto.setTitle("회원가입을 축하드립니다.");
            mDto.setContents(CmmUtil.nvl(pDTO.getName()) + "님의 회원가입을 진심으로 축하드립니다.");

            emailService.doSendMail(mDto);
        }

        log.info("{}.insertUserInfo End!", this.getClass().getName());

        return res;
    }

    @Override
    public UserDTO getEmailExists(UserDTO pDTO) throws Exception {

        log.info("{}.getEmailExists Start!", this.getClass().getName());

        UserDTO rDTO = Optional.ofNullable(userMapper.getUserEmailExists(pDTO)).orElseGet(UserDTO::new);

        if (CmmUtil.nvl(rDTO.getExistsYn()).equals("N")) {

            int authNumber = ThreadLocalRandom.current().nextInt(100000, 1000000);

            log.info("authNumber : {}", authNumber);

            MailDTO dto = new MailDTO();

            dto.setTitle("이메일 중복확인 발송메일");
            dto.setContents("인증번호는 " + authNumber + " 입니다.");
            dto.setToMail(EncryptUtil.decAES128BCBC(CmmUtil.nvl(pDTO.getEmail())));

            emailService.doSendMail(dto);
            dto = null;

            rDTO.setAuthNumber(authNumber);
        }

        log.info("{}.getUserEmailExists End!", this.getClass().getName());

        return rDTO;
    }

}



