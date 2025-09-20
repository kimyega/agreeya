package kopo.poly.kpaas.service.impl;

import kopo.poly.kpaas.dto.ContractDTO;
import kopo.poly.kpaas.dto.ContractUploadDTO;
import kopo.poly.kpaas.service.IContractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractService implements IContractService {

    private final String uploadDir = "C:/kpaas/uploads/";

    @Override
    public String saveFile(ContractUploadDTO uploadDTO) throws IOException {
        if (uploadDTO.getFile() == null || uploadDTO.getFile().isEmpty()) {
            throw new IOException("파일이 비어있습니다.");
        }

        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String fileName = System.currentTimeMillis() + "_" + uploadDTO.getFile().getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        Files.copy(uploadDTO.getFile().getInputStream(), filePath);
        log.info("파일 저장 완료: {}", filePath.toString());

        return filePath.toString();
    }

    @Override
    public String extractTextFromImage(ContractUploadDTO uploadDTO) throws Exception {
        // TODO: Google Cloud Vision API 호출
        log.info("OCR 처리 중: {}", uploadDTO.getFile().getOriginalFilename());

        return "OCR 결과 텍스트 예시";
    }

    @Override
    public void saveContract(ContractDTO dto) throws Exception {
        // TODO: DB 저장 (JPA/MyBatis)
        log.info("DB 저장 - userId: {}, countryId: {}, file: {}",
                dto.getUserId(), dto.getCountryId(), dto.getOriginalFileUrl());
    }
}
