package kopo.poly.kpaas.service;

import kopo.poly.kpaas.dto.CaseDTO;
import java.util.List;

public interface ICaseService {

    /**
     * 계약 ID 기반 유사사례 조회
     *
     * @param pDTO 계약 ID를 담은 DTO
     * @return 유사사례 리스트
     * @throws Exception
     */
    List<CaseDTO> getSimilarCases(CaseDTO pDTO) throws Exception;

}
