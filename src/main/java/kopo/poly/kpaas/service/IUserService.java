package kopo.poly.kpaas.service;

import jakarta.servlet.http.HttpServletRequest;
import kopo.poly.kpaas.dto.MailDTO;
import kopo.poly.kpaas.dto.UserDTO;

public interface IUserService {

    int doSendMail(MailDTO pDTO);

    UserDTO getUserByEmail(String email) throws Exception;

    int updatePassword(UserDTO pDTO) throws Exception;

    UserDTO getUserByNameAndPhone(String name, String tel) throws Exception;

    String maskEmail(String email);

    // ✅ 이메일 찾기용 SMS 인증코드 발송
    void sendFindEmailCode(HttpServletRequest request, String name, String tel);

}
