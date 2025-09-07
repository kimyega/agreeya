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
        log.info("🟢 login() 호출: {}", pDTO);

        // ✅ Mapper에서 email + password 조건으로 바로 조회
        UserDTO rDTO = userMapper.getLogin(pDTO);

        if (rDTO != null) {
            log.info("✅ 로그인 성공: {}", rDTO.getUserId());
        } else {
            log.warn("❌ 로그인 실패 (이메일 또는 비밀번호 불일치)");
        }

        return rDTO;
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
