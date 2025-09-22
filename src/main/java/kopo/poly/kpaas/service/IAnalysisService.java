package kopo.poly.kpaas.service;

import kopo.poly.kpaas.dto.ContractDTO;

public interface IAnalysisService {

    void analyzeContract(ContractDTO pDTO) throws Exception;
}
