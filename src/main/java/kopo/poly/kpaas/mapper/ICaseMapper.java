package kopo.poly.kpaas.mapper;

import kopo.poly.kpaas.dto.CaseDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ICaseMapper {
    List<CaseDTO> getClausesByContractId(String contractId) throws Exception;
}
