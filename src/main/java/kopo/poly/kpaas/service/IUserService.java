package kopo.poly.kpaas.service;

import kopo.poly.kpaas.dto.MailDTO;
import kopo.poly.kpaas.dto.UserDTO;

public interface IUserService {

    int doSendMail(MailDTO pDTO);

    UserDTO getUserByEmail(String email) throws Exception;

    int updatePassword(UserDTO pDTO) throws Exception;

    UserDTO getUserByNameAndPhone(String name, String tel) throws Exception;

    String maskEmail(String email);

    String sendFindEmailCode(String name, String tel);

}
