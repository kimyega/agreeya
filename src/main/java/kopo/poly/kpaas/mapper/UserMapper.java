package kopo.poly.kpaas.mapper;

import kopo.poly.kpaas.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    /** userId 기반 프로필 조회 */
    UserDTO getUserProfile(String userId);

    /** 이메일 기반 조회 (로그인/비번 재설정) */
    UserDTO getUserByEmail(String email);

    /** 회원 탈퇴 */
    int deleteUser(int userId);

    /** 비밀번호 업데이트 (이메일 기준) */
    int updatePasswordByEmail(UserDTO pDTO);

    /** 인증코드 UPSERT */
    int upsertEmailVerifySqlSide(String contactType,
                                 String contactValue,
                                 String purpose,
                                 String plainCode,
                                 int expireMin);

    /** 인증코드 검증 후 삭제 */
    int verifyAndConsumeSqlSide(String contactType,
                                String contactValue,
                                String purpose,
                                String inputCode);

    /** 휴대폰으로 사용자 조회 */
    UserDTO getUserByPhone(String phone);
}
