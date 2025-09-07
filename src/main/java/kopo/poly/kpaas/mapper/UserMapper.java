package kopo.poly.kpaas.mapper;

import kopo.poly.kpaas.dto.MailDTO;
import kopo.poly.kpaas.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    /** userId 기반 프로필 조회 */
    UserDTO getLogin(UserDTO pDTO);

    UserDTO getUserProfile(UserDTO pDTO); //이름 xml똑같이 고치기
    UserDTO getUserByEmail(UserDTO pDTO) throws Exception;

    int updatePassword(UserDTO pDTO) throws Exception;

    /** 회원 탈퇴 (userId 기준) */
    int deleteUser(UserDTO pDTO);
    UserDTO getUserByNameAndPhone(UserDTO pDto) throws Exception;

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
