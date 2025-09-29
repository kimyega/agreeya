package kopo.poly.kpaas.service;

import kopo.poly.kpaas.dto.ContractClauseDTO;
import kopo.poly.kpaas.dto.ContractAnalysisSummaryDTO;
import kopo.poly.kpaas.dto.DraftDTO;
import kopo.poly.kpaas.dto.ContractDTO;


import java.util.List;

public interface IDraftService {

    // 특정 contractId 기준으로 조항 가져오기
    List<ContractClauseDTO> getClausesByContractId(ContractDTO pDTO) throws Exception;

    // 특정 contractId 기준으로 요약 가져오기
    ContractAnalysisSummaryDTO getSummaryByContractId(ContractDTO pDTO) throws Exception;

    // clauses + summary 기반으로 GPT에 요청 → KR/EN/JP 계약서 초안 반환
    DraftDTO generateDraftContract(ContractDTO pDTO) throws Exception;
}
