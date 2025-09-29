package kopo.poly.kpaas.mapper;

import kopo.poly.kpaas.dto.ContractAnalysisSummaryDTO;
import kopo.poly.kpaas.dto.ContractClauseDTO;
import kopo.poly.kpaas.dto.ContractDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IDraftMapper {

    ContractAnalysisSummaryDTO getSummaryByContractId(ContractDTO pDTO);

    List<ContractClauseDTO> getClausesByContractId(ContractDTO pDTO);


}
