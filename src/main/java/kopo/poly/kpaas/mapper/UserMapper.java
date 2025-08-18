package kopo.poly.kpaas.mapper;

import kopo.poly.kpaas.dto.AuthCodeDTO;
import kopo.poly.kpaas.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    void updatePasswordByEmail(UserDTO userDTO);

    void updateEmailCodeByEmail(AuthCodeDTO authCodeDTO);

    UserDTO getUserByEmail(String userEmail);
}

