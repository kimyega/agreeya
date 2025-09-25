package kopo.poly.kpaas.mapper;

import kopo.poly.kpaas.dto.ContractDTO;
import kopo.poly.kpaas.dto.ContractClauseDTO;
import kopo.poly.kpaas.dto.ContractAnalysisSummaryDTO;
import kopo.poly.kpaas.dto.LawDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface IAnalysisMapper {


    // 계약서 1개만 조회 (selectOne)
    ContractDTO getContractById(ContractDTO pDTO);

    // 국가별 법령 여러 개 조회 (selectList)
    List<LawDTO> getLawsByCountryId(LawDTO pDTO);

    int insertClauseResult(ContractClauseDTO pDTO);

    int insertSummaryResult(ContractAnalysisSummaryDTO pDTO);

}
