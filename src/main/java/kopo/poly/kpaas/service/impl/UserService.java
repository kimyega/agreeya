package kopo.poly.kpaas.service.impl;

import kopo.poly.kpaas.dto.UserDTO;
import kopo.poly.kpaas.mapper.UserMapper;
import kopo.poly.kpaas.service.IUserService;
import kopo.poly.kpaas.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserMapper userMapper;
    private final JavaMailSender mailSender;

    /** 로그인 */
    @Override
    public UserDTO login(UserDTO pDTO) throws Exception {
        String email = pDTO.getEmail();          // ✅ 평문 이메일 그대로 사용
        String password = pDTO.getPassword();

        // String emailEnc = EncryptUtil.encAES128CBC(email);  // ❌ 테스트용 주석 처리
        String pwEnc = EncryptUtil.encHashSHA256(password);    // ✅ 비밀번호는 해시 비교 유지

        // DB 조회 (평문 이메일로 검색)
        UserDTO rDTO = userMapper.getUserByEmail(
                UserDTO.builder()
                        .email(email)   // 암호화 대신 평문 그대로 전달
                        .build()
        );

        if (rDTO == null) {
            log.warn("❌ 로그인 실패 - 사용자 없음");
            return null;
        }

        // 비밀번호 확인
        if (rDTO.getPassword() != null &&
                (rDTO.getPassword().equals(password) || rDTO.getPassword().equals(pwEnc))) {
            log.info("✅ 로그인 성공 userId={}", rDTO.getUserId());
            return rDTO;
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

    /** 비밀번호 변경 */
    @Override
    public int updatePassword(UserDTO pDTO) throws Exception {
        pDTO.setPassword(EncryptUtil.encHashSHA256(pDTO.getPassword()));
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
}
