package kopo.poly.kpaas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.kpaas.dto.*;
import kopo.poly.kpaas.infra.NcosObjectService;
import kopo.poly.kpaas.infra.NcosPresignService;
import kopo.poly.kpaas.service.IAnalysisService;
import kopo.poly.kpaas.service.ICaseService;
import kopo.poly.kpaas.service.IContractService;
import kopo.poly.kpaas.service.ICountryService;
import kopo.poly.kpaas.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


import java.util.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/contract")
@Controller
public class ContractController {

    private final ICountryService countryService;
    private final IContractService contractService; // ✅ 서비스 주입

    private final IAnalysisService analysisService;
    private final ICaseService caseService;

    // ncos 관련 서비스
    private final NcosPresignService ncosPresignService;
    private final NcosObjectService ncosObjectService;


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

    @PostMapping("/processOcr")
    @ResponseBody
    public ResultDTO processOcr(HttpServletRequest request, HttpSession session) {
        try {
            String imageUrl = request.getParameter("imageUrl");
            if (imageUrl == null || imageUrl.isEmpty()) {
                return ResultDTO.builder()
                        .result(0)
                        .msg("imageUrl 파라미터가 필요합니다.")
                        .build();
            }

            log.info("📄 /processOcr 요청: imageUrl={}", imageUrl);

            ContractUploadDTO uploadDTO = ContractUploadDTO.builder()
                    .fileUrl(imageUrl)
                    .build();

            // OCR 실행
            String ocrText = contractService.extractTextFromImage(uploadDTO);

            // ✅ 세션 저장 DTO 생성
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

            ContractDTO rDTO = ContractDTO.builder()
                    .userId(userId)
                    .originalFileUrl(imageUrl)
                    .ocrText(ocrText)
                    .build();

            // 세션 속성명 통일 → saveCountry에서 사용하는 이름으로 저장
            session.setAttribute("SS_CONTRACT_DRAFT", rDTO);

            return ResultDTO.builder()
                    .result(1)
                    .msg("OCR 완료 및 세션 저장")
                    .data(ocrText)
                    .build();

        } catch (Exception e) {
            log.error("❌ OCR 처리 실패", e);
            return ResultDTO.builder()
                    .result(-1)
                    .msg("OCR 처리 중 오류: " + e.getMessage())
                    .build();
        }
    }



    @PostMapping("/saveCountry")
    @ResponseBody
    public ResponseEntity<ResultDTO> saveCountry(HttpServletRequest request, HttpSession session) {
        log.info("📄 /saveCountry 요청 시작");

        String countryCode = request.getParameter("countryCode");
        log.info("선택 국가 코드: {}", countryCode);

        ContractDTO dto = (ContractDTO) session.getAttribute("SS_CONTRACT_DRAFT");

        if (dto == null) {
            log.warn("⚠️ 세션에 SS_CONTRACT_DRAFT 없음");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResultDTO.builder()
                            .result(0)
                            .msg("세션에 계약서 초안이 없습니다. 먼저 업로드하세요.")
                            .build());
        }

        try {
            // ✅ 코드로 DB에서 countryId 조회
            CountryDTO pDTO = CountryDTO.builder()
                    .countryCode(countryCode)
                    .build();

            CountryDTO rDTO = countryService.getCountryByCode(pDTO);

            if (rDTO == null || rDTO.getCountryId() == null) {
                log.warn("⚠️ 국가 조회 실패: {}", countryCode);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResultDTO.builder()
                                .result(0)
                                .msg("유효하지 않은 국가 코드입니다.")
                                .build());
            }

            String countryId = String.valueOf(rDTO.getCountryId());

            // 세션 contractDraft에 countryId 세팅
            dto.setCountryId(countryId);
            log.info("세션 contractDraft에 countryId 세팅 완료: {}", countryId);

            // DB 저장
            contractService.saveContract(dto);
            log.info("DB 저장 완료");

            // 세션 정리
            session.removeAttribute("SS_CONTRACT_DRAFT");
            log.info("세션 contractDraft 제거 완료");

            return ResponseEntity.ok(ResultDTO.builder()
                    .result(1)
                    .msg("계약서 저장 성공")
                    .data(countryId) // countryId 반환
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



    @PostMapping("/analyze")
    @ResponseBody
    public String analyzeContract(HttpSession session) {
        log.info("📄 /analyze 요청 시작");

        try {
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
            if (userId.isEmpty()) {
                log.warn("⚠️ 로그인 정보 없음 → 분석 불가");
                return "fail"; // 단순 문자열 리턴
            }

            // 최신 계약서 조회
            ContractDTO latest = contractService.getLatestContractByUserId(
                    ContractDTO.builder().userId(userId).build()
            );

            if (latest == null || latest.getCountryId() == null) {
                log.warn("⚠️ 계약서 또는 국가 정보 없음 → 분석 불가");
                return "fail"; // 단순 문자열 리턴
            }

            // 최종 DTO 조립
            ContractDTO dto = ContractDTO.builder()
                    .contractId(latest.getContractId())
                    .userId(userId)
                    .countryId(latest.getCountryId()) // 세션 대신 DB 값 사용
                    .build();

            log.info("📌 분석 시작: contractId={}, userId={}, countryId={}",
                    dto.getContractId(), dto.getUserId(), dto.getCountryId());

            // 서비스 호출
            analysisService.analyzeContract(dto);
            session.setAttribute("SS_CONTRACT_ID", dto.getContractId());

            log.info("✅ 분석 완료: contractId={}", dto.getContractId());

            return "success"; // ✅ 프론트에서 기대하는 문자열
        } catch (Exception e) {
            log.error("❌ 분석 실패", e);
            return "fail"; // 에러 발생 시 단순 실패 문자열
        }
    }

    /**
     * 홈화면 이동 시 → 세션 + 이미지 데이터 삭제
     */
    @ResponseBody
    @PostMapping("/cancelNation")
    public String cancelNation(HttpSession session) {
        log.info("{}.cancelNation Start!", this.getClass().getName());

        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
        if (userId.isEmpty()) {
            log.warn("⚠️ 로그인 세션 없음 → 삭제 불가");
            return "login_required";
        }

        ContractDTO sDTO = (ContractDTO) session.getAttribute("SS_CONTRACT_DRAFT");
        if (sDTO != null) {
            String imageUrl = CmmUtil.nvl(sDTO.getOriginalFileUrl());
            log.info("📌 삭제할 이미지 URL = {}", imageUrl);

            try {
                ncosObjectService.deleteObject(imageUrl);
            } catch (Exception e) {
                log.error("❌ Object Storage 삭제 실패", e);
                return "delete_failed";
            }
        }

        // 세션 정리
        session.removeAttribute("SS_CONTRACT_DRAFT");

        log.info("{}.cancelNation End!", this.getClass().getName());

        return "success";
    }

    // 데이터 조회@ResponseBody
    @PostMapping("/similar/data")
    @ResponseBody
    public List<CaseDTO> getSimilarCases(HttpSession session) throws Exception {
        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
        String countryId = CmmUtil.nvl((String) session.getAttribute("SS_COUNTRY_ID"));

        log.info("▶ 유사사례 조회 userId={} countryId={}", userId, countryId);

        if (userId.isEmpty() || countryId.isEmpty()) {
            log.warn("⚠️ 세션 값 없음 → 유사사례 조회 불가");
            return Collections.emptyList();
        }

        // 최신 계약서 조회
        ContractDTO latest = contractService.getLatestContractByUserId(
                ContractDTO.builder().userId(userId).build()
        );

        if (latest == null || latest.getContractId() == null) {
            log.warn("⚠️ 업로드된 계약서 없음 → 유사사례 조회 불가");
            return Collections.emptyList();
        }

        CaseDTO pDTO = CaseDTO.builder()
                .contractId(latest.getContractId())
                .countryId(countryId)
                .build();

        return Optional.ofNullable(caseService.getSimilarCases(pDTO))
                .orElseGet(Collections::emptyList);
    }

    @GetMapping("/result")
    public String result(HttpSession session, Model model) throws Exception {
        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

        if (userId.isEmpty()) {
            log.warn("⚠️ 로그인 세션 없음 → 결과 화면 진입 불가");
            return "redirect:/login"; // 로그인 페이지로 돌려보내도 됨
        }

        // 최신 계약서 조회
        ContractDTO latest = contractService.getLatestContractByUserId(
                ContractDTO.builder().userId(userId).build()
        );

        if (latest != null && latest.getContractId() != null) {
            log.info("📄 계약서 분석 결과 화면 호출, contractId={}", latest.getContractId());
            model.addAttribute("contractId", latest.getContractId());
        } else {
            log.warn("⚠️ 결과 화면 표시할 계약서 없음");
            model.addAttribute("contractId", "");
        }

        return "contract/result";
    }



}

