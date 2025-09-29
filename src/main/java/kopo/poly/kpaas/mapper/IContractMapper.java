package kopo.poly.kpaas.mapper;

import kopo.poly.kpaas.dto.ContractAnalysisSummaryDTO;
import kopo.poly.kpaas.dto.ContractClauseDTO;
import kopo.poly.kpaas.dto.ContractDTO;
import kopo.poly.kpaas.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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

    // 특정 유저의 계약 리스트 조회
    List<ContractDTO> findByUserId(UserDTO pDTO) throws Exception;

    // 특정 계약의 분석 요약 조회
    ContractAnalysisSummaryDTO findSummaryByContractId(ContractDTO pDTO) throws Exception;
}


