package kopo.poly.kpaas.service.impl;

import kopo.poly.kpaas.dto.CountryDTO;
import kopo.poly.kpaas.mapper.ICountryMapper;
import kopo.poly.kpaas.service.ICountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CountryService implements ICountryService {

    private final ICountryMapper countryMapper;
    @Override
    public CountryDTO getCountryByCode(CountryDTO pDTO) throws Exception {
        log.info("🔍 getCountryByCode start: {}", pDTO.getCountryCode());

        CountryDTO rDTO = Optional.ofNullable(countryMapper.getCountryByCode(pDTO))
                .orElseGet(CountryDTO::new);

        log.info("✅ getCountryByCode end: {} -> {}", pDTO.getCountryCode(), rDTO.getCountryId());
        return rDTO;
    }
}


