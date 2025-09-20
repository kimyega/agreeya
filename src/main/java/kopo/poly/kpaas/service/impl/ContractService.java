package kopo.poly.kpaas.service.impl;

import kopo.poly.kpaas.dto.ContractDTO;
import kopo.poly.kpaas.mapper.IContractMapper;
import kopo.poly.kpaas.service.IContractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractService implements IContractService {

    private final IContractMapper contractMapper;

    @Override
    public void deleteContractByUserAndCountry(ContractDTO pDTO) throws Exception {
        log.info("deleteContractByUserAndCountry start, userId={}, countryId={}",
                pDTO.getUserId(), pDTO.getCountryId());
        contractMapper.deleteContractByUserAndCountry(pDTO);
    }
}


