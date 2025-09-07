package kopo.poly.kpaas.service;

import jakarta.servlet.http.HttpServletRequest;
import kopo.poly.kpaas.dto.MailDTO;
import kopo.poly.kpaas.dto.UserDTO;

public interface IUserService {
    UserDTO login(UserDTO pDTO) throws Exception;

    // 마이페이지 프로필 조회
    UserDTO getUserProfile(UserDTO pDTO) throws Exception;
    int doSendMail(MailDTO pDTO);

    // 회원 탈퇴
    int deleteUser(UserDTO pDTO) throws Exception;
    UserDTO getUserByEmail(String email) throws Exception;

    // 비밀번호 변경
    int updatePassword(UserDTO pDTO) throws Exception;

    UserDTO getUserByNameAndPhone(String name, String tel) throws Exception;

    String maskEmail(String email);

    // ✅ 이메일 찾기용 SMS 인증코드 발송
    void sendFindEmailCode(HttpServletRequest request, String name, String tel);

}
