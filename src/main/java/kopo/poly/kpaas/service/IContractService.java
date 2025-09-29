package kopo.poly.kpaas.service;

import kopo.poly.kpaas.dto.ContractDTO;
import kopo.poly.kpaas.dto.ContractResultDTO;
import kopo.poly.kpaas.dto.ContractUploadDTO;
import kopo.poly.kpaas.dto.UserDTO;

import java.util.List;

public interface IContractService {


    /**
     * 업로드된 ContractUploadDTO를 받아 파일 저장 후 경로 반환
     * @param uploadDTO 업로드 DTO
     * @return 저장된 파일 경로
     * @throws Exception
     */
    String saveFile(ContractUploadDTO uploadDTO) throws Exception;


    /**
     * ContractUploadDTO를 받아 OCR 처리
     * @param uploadDTO 업로드 DTO
     * @return 추출된 텍스트
     * @throws Exception
     */
    String extractTextFromImage(ContractUploadDTO uploadDTO) throws Exception;

    /**
     * ContractDTO를 DB에 저장
     * @param dto 저장할 계약서 DTO
     */
    void saveContract(ContractDTO dto) throws Exception;

    ContractDTO getLatestContractByUserId(ContractDTO pDTO) throws Exception;

    ContractResultDTO getContractResultByContractId(ContractDTO pDTO) throws Exception;

    ContractDTO getContractById(ContractDTO pDTO) throws Exception;

    List<ContractDTO> getContractsWithSummary(UserDTO pDTO) throws Exception;

}
