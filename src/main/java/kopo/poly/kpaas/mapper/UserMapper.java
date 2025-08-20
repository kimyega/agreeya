package kopo.poly.kpaas.mapper;

import kopo.poly.kpaas.dto.EmailVerifyDTO;
import kopo.poly.kpaas.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 사용자/인증 관련 DB 접근 (MyBatis)
 * - users: 사용자 정보 조회/수정
 * - email_verify: 인증코드 저장/검증/삭제 (SQL에서 해시·만료 처리)
 */
@Mapper
public interface UserMapper {

    /* ===== users ===== */

    /** 이메일(대소문자 무시)로 사용자 조회 — 존재/주인 확인 */
    UserDTO getUserByEmail(@Param("email") String email);

    /** 이메일 기준 비밀번호 해시 업데이트 */
    void updatePasswordByEmail(UserDTO userDTO);


    /* ===== email_verify (SQL이 해시/만료/검증까지 처리) ===== */

    /**
     * 인증코드 업서트(재발송 포함)
     * - plainCode: 평문 인증코드 (DB에서 SHA2 해시로 저장)
     * - expireMin: 유효 분(min)
     */
    void upsertEmailVerifySqlSide(@Param("email") String email,
                                  @Param("purpose") String purpose,
                                  @Param("plainCode") String plainCode,
                                  @Param("expireMin") int expireMin);

    /**
     * 인증코드 검증 + 1회성 소모(삭제)
     * - 조건 일치 + 유효시간 내이면 해당 행을 삭제하고 1 반환
     * - 불일치/만료/없음이면 0 반환
     */
    int verifyAndConsumeSqlSide(@Param("email") String email,
                                @Param("purpose") String purpose,
                                @Param("inputCode") String inputCode);


}
