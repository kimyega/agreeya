package kopo.poly.kpaas.mapper;

import kopo.poly.kpaas.dto.CountryDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ICountryMapper {
    CountryDTO getCountryByCode(CountryDTO pDTO) throws Exception; // 새 메서드
}

