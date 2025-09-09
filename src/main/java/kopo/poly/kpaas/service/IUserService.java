package kopo.poly.kpaas.service;

import kopo.poly.kpaas.dto.UserDTO;

public interface IUserService {

    UserDTO getUserByEmail(UserDTO pDTO) throws Exception;
    UserDTO getUserLogin(UserDTO pDTO) throws Exception;

    UserDTO getUserProfile(UserDTO pDTO) throws Exception;

    int deleteUser(UserDTO pDTO) throws Exception;

    UserDTO getUserByPhone (UserDTO pDTO) throws Exception;

    int updatePassword(UserDTO pDTO) throws Exception;

    UserDTO getUserByNameAndPhone(UserDTO pDTO) throws Exception;


    UserDTO getEmailExists (UserDTO pDTO) throws Exception;

    String sendFindEmailCode(UserDTO pDTO); // name, tel도 DTO에서 받음
}
