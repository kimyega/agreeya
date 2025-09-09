package kopo.poly.kpaas.service;

import kopo.poly.kpaas.dto.UserDTO;

public interface IUserService {

    UserDTO getUserlogin(String email, String password) throws Exception;

    UserDTO getUserProfile(String userId) throws Exception;

    int deleteUser(String userId) throws Exception;


    UserDTO getUserByEmail(UserDTO pDTO) throws Exception;

    int updatePassword(UserDTO pDTO) throws Exception;

    UserDTO getUserByNameAndPhone(UserDTO pDTO) throws Exception;

    String sendFindEmailCode(UserDTO pDTO); // name, tel도 DTO에서 받음
}
