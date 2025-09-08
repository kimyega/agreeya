package kopo.poly.kpaas.service;

import jakarta.servlet.http.HttpServletRequest;
import kopo.poly.kpaas.dto.MailDTO;
import kopo.poly.kpaas.dto.UserDTO;

public interface IUserService {

    // 로그인
    UserDTO getUserLogin(UserDTO pDTO) throws Exception;

    // 마이페이지 프로필 조회
    UserDTO getUserProfile(UserDTO pDTO) throws Exception;

    // 회원 탈퇴
    int deleteUser(UserDTO pDTO) throws Exception;

    // 비밀번호 변경
    int updatePassword(UserDTO pDTO) throws Exception;

    // 이메일로 사용자 조회
    UserDTO getUserByEmail(UserDTO pDTO) throws Exception;

    // 휴대폰으로 사용자 조회
    UserDTO getUserByPhone(UserDTO pDTO) throws Exception;

    // 메일 발송
    int doSendMail(MailDTO pDTO);

    // 이메일 찾기용 SMS 인증코드 발송
    void sendFindEmailCode(HttpServletRequest request, String name, String tel);
}
