package kopo.poly.kpaas.mapper;

import kopo.poly.kpaas.dto.LawDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ILawMapper {

    int insertLaw(LawDTO pDTO) throws Exception;

}
