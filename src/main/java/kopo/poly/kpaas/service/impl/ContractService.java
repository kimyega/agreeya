package kopo.poly.kpaas.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.vision.v1.*;
import kopo.poly.kpaas.config.NcosProperties;
import kopo.poly.kpaas.dto.*;
import kopo.poly.kpaas.infra.NcosPresignService;
import kopo.poly.kpaas.mapper.IContractMapper;
import kopo.poly.kpaas.service.IContractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractService implements IContractService {

    private final NcosPresignService ncosPresignService; // v2 Presigner 사용
    private final NcosProperties ncosProperties;

    private final IContractMapper contractMapper;
    private final ObjectMapper objectMapper;


    @Override
    public String saveFile(ContractUploadDTO uploadDTO) throws Exception {
        if (uploadDTO.getFile() == null || uploadDTO.getFile().isEmpty()) {
            throw new Exception("파일이 없습니다.");
        }

        String folder = "contracts";
        String contentType = Optional.ofNullable(uploadDTO.getFile().getContentType())
                .orElse("application/octet-stream");

        log.info("[ContractService] Presigned URL 생성 시도 - userId: {}, file: {}",
                uploadDTO.getUserId(), uploadDTO.getFile().getOriginalFilename());

        // Presigned URL 생성
        NcosPresignService.PresignedUpload presigned =
                ncosPresignService.createUploadUrl(folder, contentType);

        log.info("[ContractService] Presigned URL 발급 완료 - uploadUrl: {}, publicUrl: {}",
                presigned.uploadUrl(), presigned.publicUrl());

        // ❌ DB에 저장하지 않음
        // Presigned URL만 반환
        return presigned.publicUrl();
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
    public void saveContract(ContractDTO dto) throws Exception {
        log.info("DB 저장 실행 - userId={}, countryId={}, file={}",
                dto.getUserId(), dto.getCountryId(), dto.getOriginalFileUrl());

        if (dto.getUserId() == null || dto.getUserId().isEmpty()
                || dto.getCountryId() == null || dto.getCountryId().isEmpty()) {
            throw new IllegalArgumentException("userId와 countryId는 필수 값입니다.");
        }

        contractMapper.insertContract(dto);

        log.info("✅ 계약서 저장 완료: {}", dto);
    }

    @Override
    public ContractDTO getLatestContractByUserId(ContractDTO pDTO) throws Exception {
        return contractMapper.getLatestContractByUserId(pDTO);
    }

    @Override
    public ContractResultDTO getContractResultByContractId(ContractDTO pDTO) throws Exception {

        ContractAnalysisSummaryDTO summary = contractMapper.getContractSummaryByContractId(pDTO);
        java.util.List<ContractClauseDTO> clauses = contractMapper.getContractClausesByContractId(pDTO);

        ContractResultDTO rDTO = ContractResultDTO.builder()
                .summary(summary)
                .clauses(clauses)
                .build();

        return rDTO;
    }
    @Override
    public ContractDTO getContractById(ContractDTO pDTO) throws Exception {
        if (pDTO == null || pDTO.getContractId() == null || pDTO.getContractId().isEmpty()) {
            throw new IllegalArgumentException("contractId는 필수 값입니다.");
        }

        log.info("📌 계약서 단건 조회 실행 - contractId={}", pDTO.getContractId());

        ContractDTO rDTO = Optional.ofNullable(
                contractMapper.getContractById(pDTO)
        ).orElseGet(ContractDTO::new);

        log.info("✅ 계약서 단건 조회 완료: {}", rDTO);

        return rDTO;
    }

    @Override
    public List<ContractDTO> getContractsWithSummary(UserDTO pDTO) throws Exception {
        List<ContractDTO> contracts = Optional.ofNullable(contractMapper.findByUserId(pDTO))
                .orElse(new ArrayList<>());
        List<ContractDTO> result = new ArrayList<>();

        for (ContractDTO c : contracts) {
            ContractAnalysisSummaryDTO summary = contractMapper.findSummaryByContractId(c);
            int riskCount = 0, totalRiskLevel = 0;

            if (summary != null && summary.getRiskChartData() != null && !summary.getRiskChartData().isEmpty()) {
                try {
                    Map<String, Integer> riskData = objectMapper.readValue(summary.getRiskChartData(), Map.class);
                    riskCount = riskData.values().stream().mapToInt(Integer::intValue).sum();
                } catch (Exception e) {
                    log.warn("riskChartData 파싱 실패: {}", summary.getRiskChartData(), e);
                    riskCount = 0;
                }
            }

            totalRiskLevel = summary.getTotalRiskLevel();
            String riskLevel = (totalRiskLevel <= 40) ? "안전" : (totalRiskLevel <= 70) ? "보통" : "높음";

            result.add(ContractDTO.builder()
                    .contractId(c.getContractId())
                    .createdAt(c.getCreatedAt())
                    .riskCount(riskCount)
                    .riskLevel(riskLevel)
                    .build()
            );
        }
        return result;
    }

    @Override
    public int deleteContractById(ContractDTO pDTO) throws Exception {
        return contractMapper.deleteContractById(pDTO);
    }

}

