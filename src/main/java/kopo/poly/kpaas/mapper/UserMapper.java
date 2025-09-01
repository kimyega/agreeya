package kopo.poly.kpaas.mapper;

import kopo.poly.kpaas.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    // 기존 사용자 관련은 그대로
    UserDTO getUserByEmail(@Param("email") String email);
    int updatePasswordByEmail(UserDTO userDTO);


    UserDTO getUserByPhone(@Param("phone") String phone);

    // === 인증 저장: 업서트(SQL에서 SHA2 해시 + 만료 계산) ===
    int upsertEmailVerifySqlSide(@Param("contactType") String contactType,
                                 @Param("contactValue") String contactValue,
                                 @Param("purpose") String purpose,
                                 @Param("plainCode") String plainCode,
                                 @Param("expireMin") int expireMin);

    // === 인증 검증: 성공 시 1행 삭제 ===
    int verifyAndConsumeSqlSide(@Param("contactType") String contactType,
                                @Param("contactValue") String contactValue,
                                @Param("purpose") String purpose,
                                @Param("inputCode") String inputCode);



}
