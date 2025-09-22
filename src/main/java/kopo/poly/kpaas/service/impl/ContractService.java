package kopo.poly.kpaas.service.impl;

import kopo.poly.kpaas.config.NcosProperties;
import kopo.poly.kpaas.dto.ContractDTO;
import kopo.poly.kpaas.dto.ContractUploadDTO;
import kopo.poly.kpaas.infra.NcosPresignService;
import kopo.poly.kpaas.service.IContractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;
import kopo.poly.kpaas.dto.ContractUploadDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


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

    /**
     * Google Vision API로 이미지 URL 기반 OCR 수행
     */
    @Override
    public String extractTextFromImage(ContractUploadDTO uploadDTO) throws Exception {
        String imageUrl = uploadDTO.getFileUrl();
        log.info("OCR 처리 시작, URL: {}", imageUrl);

        // 요청할 이미지 생성
        Image img = Image.newBuilder().setSource(Image.Source.newBuilder().setImageUri(imageUrl).build()).build();

        // OCR 기능 지정
        Feature feat = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build();

        // 요청 객체 생성
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feat)
                .setImage(img)
                .build();

        List<AnnotateImageRequest> requests = new ArrayList<>();
        requests.add(request);

        // Vision API 호출
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            AnnotateImageResponse response = client.batchAnnotateImages(requests).getResponses(0);

            if (response.hasError()) {
                log.error("OCR 오류: {}", response.getError().getMessage());
                throw new Exception("OCR 처리 실패: " + response.getError().getMessage());
            }

            String extractedText = response.getFullTextAnnotation().getText();
            log.info("OCR 완료, 결과 길이: {}", extractedText.length());
            return extractedText;
        }
    }

    @Override
    public void saveContract(ContractDTO dto) throws Exception {
        log.info("DB 저장 - userId: {}, file: {}", dto.getUserId(), dto.getOriginalFileUrl());
        // TODO: 실제 DB 저장 구현
    }
}
