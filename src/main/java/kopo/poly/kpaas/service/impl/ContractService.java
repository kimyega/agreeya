package kopo.poly.kpaas.service.impl;

import kopo.poly.kpaas.config.NcosProperties;
import kopo.poly.kpaas.dto.ContractDTO;
import kopo.poly.kpaas.dto.ContractUploadDTO;
import kopo.poly.kpaas.infra.NcosPresignService;
import kopo.poly.kpaas.service.IContractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ContractService implements IContractService {

    private final NcosPresignService ncosPresignService; // v2 Presigner 사용
    private final NcosProperties ncosProperties;


    @Override
    public String saveFile(ContractUploadDTO uploadDTO) throws Exception {
        if (uploadDTO.getFile() == null || uploadDTO.getFile().isEmpty()) {
            throw new Exception("파일이 없습니다.");
        }

        String folder = "contracts"; // 원하는 폴더명
        String contentType = Optional.ofNullable(uploadDTO.getFile().getContentType())
                .orElse("application/octet-stream");

        log.info("[ContractService] Presigned URL 생성 시도 - userId: {}, file: {}", uploadDTO.getUserId(), uploadDTO.getFile().getOriginalFilename());

        // Presigned URL 생성
        NcosPresignService.PresignedUpload presigned = ncosPresignService.createUploadUrl(folder, contentType);

        log.info("[ContractService] Presigned URL 발급 완료 - uploadUrl: {}, publicUrl: {}", presigned.uploadUrl(), presigned.publicUrl());

        // 실제 업로드는 프론트엔드에서 URL로 PUT 요청하거나,

        return presigned.publicUrl(); // 최종 접근 가능한 URL 반환
    }

    @Override
    public String extractTextFromImage(ContractUploadDTO uploadDTO) throws Exception {
        log.info("OCR 처리 중: {}", uploadDTO.getFile().getOriginalFilename());
        // TODO: 실제 OCR 구현
        return "OCR 결과 예시";
    }

    @Override
    public void saveContract(ContractDTO dto) throws Exception {
        log.info("DB 저장 - userId: {}, file: {}", dto.getUserId(), dto.getOriginalFileUrl());
        // TODO: 실제 DB 저장 구현
    }
}
