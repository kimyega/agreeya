package kopo.poly.kpaas.mapper;

import kopo.poly.kpaas.dto.AnalysisSummaryDto;
import kopo.poly.kpaas.dto.ClauseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ContractMapper {
    AnalysisSummaryDto getSummary(@Param("userId") int userId,
                                  @Param("contractId") int contractId);
    List<ClauseDto> getClauses(@Param("contractId") int contractId);

    // (옵션) 저장용
    int upsertSummary(AnalysisSummaryDto dto); // 필요 시 XML에서 MERGE/ON DUP KEY UPDATE
}
