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
    Optional<ContractDTO> getContractById(ContractDTO pDTO);

    // 국가별 법령 조회
    LawDTO getLawByCountryId(LawDTO pDTO);

    // 조항별 분석 결과 저장
    int insertClauseResult(ContractClauseDTO pDTO);

    // 계약서 전체 요약 분석 결과 저장
    int insertSummaryResult(ContractAnalysisSummaryDTO pDTO);

}
