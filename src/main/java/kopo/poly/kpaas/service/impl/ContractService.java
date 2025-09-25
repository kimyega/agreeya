package kopo.poly.kpaas.service.impl;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.vision.v1.*;
import kopo.poly.kpaas.config.NcosProperties;
import kopo.poly.kpaas.dto.ContractDTO;
import kopo.poly.kpaas.dto.ContractUploadDTO;
import kopo.poly.kpaas.infra.NcosPresignService;
import kopo.poly.kpaas.mapper.IContractMapper;
import kopo.poly.kpaas.service.IContractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ContractService implements IContractService {

    private final NcosPresignService ncosPresignService; // v2 Presigner 사용
    private final NcosProperties ncosProperties;

    private final IContractMapper contractMapper;

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
        log.info("OCR 처리 중: {}",
                uploadDTO.getFile() != null ? uploadDTO.getFile().getOriginalFilename() : "URL 업로드");

        String imageUrl = uploadDTO.getFileUrl();
        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new Exception("OCR 수행을 위한 이미지 URL이 없습니다.");
        }

        log.info("Google Vision API OCR 요청 - imageUrl: {}", imageUrl);

        StringBuilder extractedText = new StringBuilder();

        // 프로젝트 루트 기준 키 파일 경로
        String keyFilePath = "keys/kpaas-vision-key.json";

        try (FileInputStream serviceAccount = new FileInputStream(keyFilePath)) {
            ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
                    .setCredentialsProvider(() -> ServiceAccountCredentials.fromStream(serviceAccount))
                    .build();

            try (ImageAnnotatorClient vision = ImageAnnotatorClient.create(settings)) {
                ImageSource imgSource = ImageSource.newBuilder().setImageUri(imageUrl).build();
                Image image = Image.newBuilder().setSource(imgSource).build();

                Feature feature = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
                AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                        .addFeatures(feature)
                        .setImage(image)
                        .build();

                BatchAnnotateImagesResponse response = vision.batchAnnotateImages(
                        java.util.Collections.singletonList(request)
                );

                for (AnnotateImageResponse res : response.getResponsesList()) {
                    if (res.hasError()) {
                        log.error("OCR 에러: {}", res.getError().getMessage());
                        throw new Exception("OCR 실패: " + res.getError().getMessage());
                    }
                    extractedText.append(res.getFullTextAnnotation().getText());
                }
            }
        }

        String result = extractedText.toString().trim();
        log.info("OCR 결과: {}", result);

        return result.isEmpty() ? "텍스트를 추출하지 못했습니다." : result;
    }

    @Override
    public ContractDTO getLatestContractByUserId(ContractDTO pDTO) throws Exception {
        return contractMapper.getLatestContractByUserId(pDTO);
    }

    @Override
    public void deleteContractByUserAndCountry(ContractDTO pDTO) throws Exception {
        log.info("deleteContractByUserAndCountry start, userId={}, countryId={}",
                pDTO.getUserId(), pDTO.getCountryId());
        contractMapper.deleteContractByUserAndCountry(pDTO);
    }
    @Override
    public void saveContract(ContractDTO dto) throws Exception {
        log.info("DB 저장 - userId: {}, file: {}", dto.getUserId(), dto.getOriginalFileUrl());
        // TODO: 실제 DB 저장 구현
    }
}
