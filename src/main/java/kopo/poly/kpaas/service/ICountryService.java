package kopo.poly.kpaas.service;

import kopo.poly.kpaas.dto.ContractDTO;
import kopo.poly.kpaas.dto.CountryDTO;

public interface ICountryService {
    CountryDTO getCountryByCode(CountryDTO pDTO) throws Exception; // 새 메서드
    }
