package kopo.poly.kpaas.mapper;

import kopo.poly.kpaas.dto.ContractAnalysisSummaryDTO;
import kopo.poly.kpaas.dto.ContractClauseDTO;
import kopo.poly.kpaas.dto.ContractDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IContractMapper {
    /**
     * 계약서 정보 DB에 저장
     * @param dto 계약서 DTO
     * @return 저장된 레코드 수
     */
    int insertContract(ContractDTO dto);

    ContractDTO getLatestContractByUserId(ContractDTO pDTO) throws Exception;

    // 계약서 요약 결과 조회
    ContractAnalysisSummaryDTO getContractSummaryByContractId(ContractDTO pDTO) throws Exception;

    // 계약서 조항 리스트 조회
    java.util.List<ContractClauseDTO> getContractClausesByContractId(ContractDTO pDTO) throws Exception;

    // IContractMapper.java
    ContractDTO getContractById(ContractDTO pDTO) throws Exception;
}


