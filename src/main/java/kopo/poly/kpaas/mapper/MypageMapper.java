package kopo.poly.kpaas.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface MypageMapper {
    Map<String, Object> selectUserProfile(@Param("userId") String userId);
}
