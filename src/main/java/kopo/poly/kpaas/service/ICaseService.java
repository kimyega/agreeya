package kopo.poly.kpaas.service;

import kopo.poly.kpaas.dto.ContractDTO;
import java.util.List;

public interface ICaseService {

    // 계약서 유사사례 조회
    List<ContractDTO> getSimilarCases(ContractDTO pDTO) throws Exception;

}
