package kopo.poly.kpaas.mapper;

import kopo.poly.kpaas.dto.ContractDTO;
import kopo.poly.kpaas.dto.ContractClauseDTO;
import kopo.poly.kpaas.dto.ContractAnalysisSummaryDTO;
import kopo.poly.kpaas.dto.LawDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface IAnalysisMapper {

    // 계약서 본문 조회
    ContractDTO getContractById(ContractDTO pDTO);
    // Optional 제거
    LawDTO getLawByCountryId(LawDTO pDTO);

    int insertClauseResult(ContractClauseDTO pDTO);

    int insertSummaryResult(ContractAnalysisSummaryDTO pDTO);

}
