package kopo.poly.kpaas.mapper;

import kopo.poly.kpaas.dto.MailDTO;
import kopo.poly.kpaas.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUserMapper {

    UserDTO getUserLogin(UserDTO pDTO) throws Exception;

    UserDTO getUserProfile(UserDTO userDTO) throws Exception;

    UserDTO getUserByPhone(String phone) throws Exception;

    int deleteUser(UserDTO pDTO) throws Exception; // ✅ 반환타입 UserDTO → int

    UserDTO getUserByEmail(UserDTO pDTO) throws Exception;

    int updatePassword(UserDTO pDTO) throws Exception;

    UserDTO getUserByNameAndPhone(UserDTO pDto) throws Exception;

}
