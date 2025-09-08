package kopo.poly.kpaas.mapper;


import kopo.poly.kpaas.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    UserDTO getUserByEmail(UserDTO pDTO) throws Exception;

    int updatePassword(UserDTO pDTO) throws Exception;

    UserDTO getUserByNameAndPhone(UserDTO pDto) throws Exception;

}
