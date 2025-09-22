package kopo.poly.kpaas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.kpaas.dto.ContractDTO;
import kopo.poly.kpaas.dto.ContractUploadDTO;
import kopo.poly.kpaas.dto.ResultDTO;
import kopo.poly.kpaas.infra.NcosPresignService;
import kopo.poly.kpaas.service.IContractService;
import kopo.poly.kpaas.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/contract")
@Controller
public class ContractController {

    private final IContractService contractService;
    private final NcosPresignService ncosPresignService;

    @GetMapping("/upload")
    public String upload() {
        log.info("📄 계약서 업로드 화면 호출");
        return "contract/upload"; // → /WEB-INF/views/contract/upload.jsp
    }

    @GetMapping("/loading")
    public String loading() {
        log.info("📄 계약서 분석 로딩 화면 호출");
        return "contract/loading"; // → contract/loading.jsp
    }

    @GetMapping("/result")
    public String result() {
        log.info("📄 계약서 분석 결과 화면 호출");
        return "contract/result"; // → contract/result.jsp
    }

    @GetMapping("/similar")
    public String similar() {
        log.info("📄 유사 사례 추천 화면 호출");
        return "contract/similarCase"; // → contract/similarCase.jsp
    }

    @GetMapping("/country")
    public String country() {
        log.info("📄 국가 선택 화면 호출");
        return "contract/country"; // → contract/country.jsp
    }

    @GetMapping("/draft")
    public String draft() {
        log.info("📄 AI 계약서 초안 화면 호출");
        return "contract/aiContract"; // → contract/aiContract.jsp
    }

    /* -------------------------------
       1) Presigned URL 발급 엔드포인트
       프론트: fileName, contentType 전달
       응답: ResultDTO.data -> Map { uploadUrl, publicUrl }
       ------------------------------- */
    @PostMapping("/getPresignedUrl")
    @ResponseBody
    public ResultDTO getPresignedUrl(HttpServletRequest request) {
        try {
            String fileName = request.getParameter("fileName");
            String contentType = request.getParameter("contentType");
            String folder = "contracts";
            String ct = Optional.ofNullable(contentType).orElse("application/octet-stream");

            NcosPresignService.PresignedUpload presigned = ncosPresignService.createUploadUrl(folder, ct);

            Map<String, String> data = new HashMap<>();
            data.put("uploadUrl", presigned.uploadUrl());
            data.put("publicUrl", presigned.publicUrl());
            data.put("key", presigned.key()); // 필요하면



            // Map → JSON 문자열 변환
            ObjectMapper mapper = new ObjectMapper();
            String jsonData = mapper.writeValueAsString(data);

            log.info("[ContractController] Presigned URL 발급 - uploadUrl={}, publicUrl={}", presigned.uploadUrl(), presigned.publicUrl());

            return ResultDTO.builder()
                    .result(1)
                    .msg("Presigned URL 발급 완료")
                    .data(jsonData)
                    .build();

        } catch (Exception e) {
            log.error("Presigned URL 생성 실패", e);
            return ResultDTO.builder()
                    .result(-1)
                    .msg("Presigned URL 생성 중 오류: " + e.getMessage())
                    .build();
        }
    }

    /* -------------------------------
       2) 업로드 처리 엔드포인트 (하나로 통합)
       - 프론트가 presigned PUT으로 업로드 완료 후 => 파일 URL 전송(fileUrl)
       - 또는 프론트가 multipart/form-data로 직접 전송 => 서버에서 처리
       ------------------------------- */
    @PostMapping("/uploadFile")
    @ResponseBody
    public ResultDTO uploadContract(HttpServletRequest request, HttpSession session) {
        log.info("📄 /uploadFile 요청 시작");

        try {
            // 1) 프론트에서 Presigned 업로드 후 public URL만 전달한 경우 (fileUrl 파라미터)
            String fileUrlParam = request.getParameter("fileUrl");
            String userIdParam = request.getParameter("userId");

            if (fileUrlParam != null && !fileUrlParam.isEmpty()) {
                log.info("프론트 업로드 방식 - fileUrl 전달됨: userId={}, fileUrl={}", userIdParam, fileUrlParam);

                // **여기서 contractService.extractTextFromImage(String fileUrl) 형태의 메서드를 사용**
                // (서비스에 해당 시그니처가 없다면 서비스에 추가하세요)
                ContractUploadDTO uploadDTO = ContractUploadDTO.builder()
                        .fileUrl(fileUrlParam) // URL 기반 업로드
                        .userId(userIdParam)
                        .build();

                String ocrText = contractService.extractTextFromImage(uploadDTO);

                ContractDTO dto = ContractDTO.builder()
                        .userId(userIdParam)
                        .originalFileUrl(fileUrlParam)
                        .ocrText(ocrText)
                        .build();

                session.setAttribute("contractDraft", dto);
                log.info("세션에 contractDraft 저장 완료 (presigned flow)");

                return ResultDTO.builder()
                        .result(1)
                        .msg("OCR 완료")
                        .data(ocrText)
                        .build();
            }

            // 2) multipart로 파일이 직접 전송된 경우 (서버에서 처리)
            if (request instanceof MultipartHttpServletRequest ||
                    (request.getContentType() != null && request.getContentType().toLowerCase().startsWith("multipart/"))) {

                MultipartHttpServletRequest multi = (MultipartHttpServletRequest) request;
                MultipartFile file = multi.getFile("file");
                String userId = multi.getParameter("userId");

                log.info("서버 업로드 방식 - multipart로 파일 수신: userId={}, file={}", userId, (file != null ? file.getOriginalFilename() : "null"));

                if (file == null || file.isEmpty()) {
                    log.warn("⚠️ multipart로 전송되었으나 파일이 비어있음");
                    return ResultDTO.builder().result(0).msg("파일이 업로드되지 않았습니다.").build();
                }

                ContractUploadDTO uploadDTO = new ContractUploadDTO();
                uploadDTO.setFile(file);
                uploadDTO.setUserId(userId);

                // saveFile 내부에서 presigned을 사용하거나 amazonS3.putObject로 올리는 구현이 되도록 구성하세요.
                String fileUrl = contractService.saveFile(uploadDTO); // 서비스가 업로드 후 public URL 반환
                log.info("파일 저장 완료 → URL: {}", fileUrl);

                String ocrText = contractService.extractTextFromImage(uploadDTO);
                ContractDTO dto = ContractDTO.builder()
                        .userId(userId)
                        .originalFileUrl(fileUrl)
                        .ocrText(ocrText)
                        .build();

                session.setAttribute("contractDraft", dto);
                log.info("세션에 contractDraft 저장 완료 (multipart flow)");

                return ResultDTO.builder()
                        .result(1)
                        .msg("OCR 완료")
                        .data(ocrText)
                        .build();
            }

            // 둘 다 아닌 경우
            log.warn("요청에서 fileUrl도 multipart 파일도 찾지 못함");
            return ResultDTO.builder().result(0).msg("업로드 방식이 올바르지 않습니다.").build();

        } catch (Exception e) {
            log.error("❌ 파일 처리 실패", e);
            return ResultDTO.builder()
                    .result(-1)
                    .msg("파일 처리 중 오류: " + e.getMessage())
                    .build();
        }
    }
    @PostMapping("/saveCountry")
    @ResponseBody
    public ResponseEntity<ResultDTO> saveCountry(HttpServletRequest request, HttpSession session) {
        log.info("📄 /saveCountry 요청 시작");
        String countryId = request.getParameter("countryId");
        log.info("선택 국가 ID: {}", countryId);

        ContractDTO dto = (ContractDTO) session.getAttribute("contractDraft");

        if (dto == null) {
            log.warn("⚠️ 세션에 contractDraft 없음");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResultDTO.builder()
                            .result(0)
                            .msg("세션에 계약서 초안이 없습니다. 먼저 업로드하세요.")
                            .build());
        }

        try {
            dto.setCountryId(countryId);
            log.info("세션 contractDraft에 countryId 세팅 완료");

            contractService.saveContract(dto);
            log.info("DB 저장 완료");

            session.removeAttribute("contractDraft");
            log.info("세션 contractDraft 제거 완료");

            return ResponseEntity.ok(ResultDTO.builder()
                    .result(1)
                    .msg("계약서 저장 성공")
                    .data(dto.getCountryId())
                    .build());

        } catch (Exception e) {
            log.error("계약서 저장 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResultDTO.builder()
                            .result(-1)
                            .msg("계약서 저장 중 오류: " + e.getMessage())
                            .build());
        }
    }

    @PostMapping("/selectNation")
    @ResponseBody
    public String selectNation(HttpServletRequest request, HttpSession session) {
        log.info("📄 /selectNation 요청 시작");
        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
        if (userId.isEmpty()) {
            log.warn("⚠️ 로그인 세션 없음 → 국가 선택 불가");
            return "login_required";
        }

        String countryId = CmmUtil.nvl(request.getParameter("countryId"));
        log.info("사용자 [{}] → 선택 국가 [{}]", userId, countryId);

        session.setAttribute("SELECTED_COUNTRY_ID", countryId);
        log.info("세션 SELECTED_COUNTRY_ID 저장 완료");

        return "success";
    }

    @PostMapping("/cancelNation")
    @ResponseBody
    public String cancelNation(HttpSession session) {
        log.info("📄 /cancelNation 요청 시작");
        session.removeAttribute("SELECTED_COUNTRY_ID");
        log.info("세션 SELECTED_COUNTRY_ID 제거 완료");
        return "success";
    }

}
