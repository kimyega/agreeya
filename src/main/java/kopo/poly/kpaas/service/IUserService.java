package kopo.poly.kpaas.service;

import kopo.poly.kpaas.dto.MailDTO;
import kopo.poly.kpaas.dto.UserDTO;

public interface IUserService {

    int doSendMail(MailDTO pDTO);

    UserDTO getUserByEmail(String email) throws Exception;

    UserDTO getUserByPhone(String phone) throws Exception;

    int updatePassword(UserDTO pDTO) throws Exception;

}
