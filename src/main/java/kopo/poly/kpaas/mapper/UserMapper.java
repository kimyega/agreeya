package kopo.poly.kpaas.mapper;

import kopo.poly.kpaas.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    // 로그인
    UserDTO getLogin(UserDTO pDTO);

    // 프로필 조회
    UserDTO getUserProfile(UserDTO pDTO);

    // 이메일로 사용자 조회
    UserDTO getUserByEmail(UserDTO pDTO);

    // 회원 탈퇴
    int deleteUser(UserDTO pDTO);

    // 비밀번호 업데이트
    int updatePasswordByEmail(UserDTO pDTO);

    // 휴대폰으로 사용자 조회
    UserDTO getUserByPhone(UserDTO pDTO);
}
