package kopo.poly.kpaas.mapper;

import kopo.poly.kpaas.dto.ContractDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ICaseMapper {

    // 특정 계약서 텍스트 조회
    String getContractTextById(String contractId) throws Exception;

    // 비교 대상 계약서 목록 (자기 자신 제외)
    List<ContractDTO> getOtherContracts(String contractId) throws Exception;
}
