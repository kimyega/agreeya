package kopo.poly.kpaas.mapper;

import kopo.poly.kpaas.dto.ContractDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
    public interface IContractMapper {
        int deleteContractByUserAndCountry(ContractDTO pDTO) throws Exception;

        ContractDTO getLatestContractByUserId(ContractDTO pDTO) throws Exception;
    }


