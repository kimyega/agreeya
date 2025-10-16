package kopo.poly.kpaas.mapper;

import kopo.poly.kpaas.dto.QaLogDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IChatbotMapper {

    int insertQaLog(QaLogDTO pDTO) throws Exception;

}
