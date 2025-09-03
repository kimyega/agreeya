package kopo.poly.kpaas.service.impl;

import kopo.poly.kpaas.dto.UserDTO;

public interface IUserService {
    UserDTO login(UserDTO pDTO) throws Exception;

    // 마이페이지 프로필 조회
    UserDTO getUserProfile(UserDTO pDTO) throws Exception;

    // 회원 탈퇴
    int deleteUser(UserDTO pDTO) throws Exception;

    // 비밀번호 변경
    int updatePassword(UserDTO pDTO) throws Exception;

    // 이메일 존재 여부
    UserDTO getUserByEmail(UserDTO pDTO) throws Exception;

    // 휴대폰으로 사용자 조회
    UserDTO getUserByPhone(UserDTO pDTO) throws Exception;
}
