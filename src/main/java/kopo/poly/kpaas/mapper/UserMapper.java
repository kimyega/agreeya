package kopo.poly.kpaas.mapper;

import kopo.poly.kpaas.dto.MailDTO;
import kopo.poly.kpaas.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    UserDTO getUserByEmail(UserDTO pDTO) throws Exception;

    int updatePassword(UserDTO pDTO) throws Exception;

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
