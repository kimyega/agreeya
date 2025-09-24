package kopo.poly.kpaas.mapper;

import kopo.poly.kpaas.dto.LawDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ILawMapper {

    int insertLaw(LawDTO pDTO) throws Exception;
    /**
     * 특정 국가의 법령만 조회
     *
     * @param countryId 국가 ID
     */
    List<LawDTO> getLawsByCountryId(String countryId) throws Exception;


}
