package kopo.poly.kpaas.service;

import kopo.poly.kpaas.dto.ContractDTO;

public interface IContractService {
        void deleteContractByUserAndCountry(ContractDTO pDTO) throws Exception;

        ContractDTO getLatestContractByUserId(ContractDTO pDTO) throws Exception;
    }

