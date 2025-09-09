package kopo.poly.kpaas.mapper;

import kopo.poly.kpaas.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUserMapper {

    UserDTO getUserLogin(UserDTO pDTO) throws Exception;

    UserDTO getUserProfile(UserDTO userDTO) throws Exception;

    UserDTO getUserByPhone(String phone) throws Exception;

    UserDTO deleteUser(String email) throws Exception;

    UserDTO getUserByEmail(UserDTO pDTO) throws Exception;

    int updatePassword(UserDTO pDTO) throws Exception;

    UserDTO getUserByNameAndPhone(UserDTO pDto) throws Exception;

    UserDTO getUserEmailExists(UserDTO pDto) throws Exception;

    int insertUser(UserDTO pDTO) throws Exception;

}
