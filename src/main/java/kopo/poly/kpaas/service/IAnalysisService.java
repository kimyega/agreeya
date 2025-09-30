package kopo.poly.kpaas.service;

import kopo.poly.kpaas.dto.ContractDTO;

import java.util.List;

public interface IAnalysisService {

    void analyzeContract(ContractDTO pDTO) throws Exception;
}
