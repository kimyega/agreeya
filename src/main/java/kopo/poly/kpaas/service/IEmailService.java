package kopo.poly.kpaas.service;

import kopo.poly.kpaas.dto.MailDTO;
import kopo.poly.kpaas.dto.UserDTO;

public interface IEmailService {

    int doSendMail(MailDTO pDTO);

    UserDTO maskEmail(UserDTO pDTO);

}