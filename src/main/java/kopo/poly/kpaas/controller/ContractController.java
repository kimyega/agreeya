package kopo.poly.kpaas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.kpaas.dto.*;
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
    private final NcosPresignService ncosPresignService;
    private final ICaseService caseService;


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

            // ✅ DB 저장 DTO 생성
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
            String countryId = CmmUtil.nvl((String) session.getAttribute("SS_COUNTRY_ID"));

            ContractDTO rDTO = ContractDTO.builder()
                    .userId(userId)
                    .countryId(countryId.isEmpty() ? null : countryId) // 아직 국가 선택 안 했으면 null
                    .originalFileUrl(imageUrl)
                    .ocrText(ocrText)
                    .build();

            // ✅ DB insert 실행
            contractService.saveContract(rDTO);



            return ResultDTO.builder()
                    .result(1)
                    .msg("OCR 완료 및 DB 저장")
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



    @PostMapping("/analyze")
    @ResponseBody
    public String analyzeContract(HttpSession session) {
        try {

            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
            String countryId = CmmUtil.nvl((String) session.getAttribute("SS_COUNTRY_ID"));
            log.info("📌 세션 값 확인: userId={}, countryId={}", userId, countryId);

            if (userId.isEmpty() || countryId.isEmpty()) {
                log.warn("⚠️ 세션 값 없음 → 분석 불가");
                return "fail";
            }


            ContractDTO cDTO = ContractDTO.builder()
                    .userId(userId)
                    .build();

            ContractDTO latest = contractService.getLatestContractByUserId(cDTO);

            if (latest == null) {
                log.warn("⚠️ 업로드된 계약서 없음");
                return "fail";
            }

            // 3. 최종 DTO 조립
            ContractDTO dto = ContractDTO.builder()
                    .contractId(latest.getContractId())
                    .userId(userId)
                    .countryId(countryId)
                    .build();

            log.info("📌 분석 시작: contractId={}, userId={}, countryId={}",
                    dto.getContractId(), dto.getUserId(), dto.getCountryId());

            // 4. 서비스 호출
            analysisService.analyzeContract(dto);
            session.setAttribute("SS_CONTRACT_ID", dto.getContractId());

            log.info("✅ 분석 완료: contractId={}", dto.getContractId());
            return "success";

        } catch (Exception e) {
            log.error("❌ 분석 실패", e);
            return "fail";
        }
    }
    @ResponseBody
    @PostMapping("/selectNation")
    public String selectNation(HttpServletRequest request, HttpSession session) throws Exception {

        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
        if (userId.isEmpty()) {
            return "login_required";
        }

        String countryCode = CmmUtil.nvl(request.getParameter("countryCode"));
        if (countryCode.isEmpty()) {
            return "fail";
        }

        CountryDTO pDTO = CountryDTO.builder()
                .countryCode(countryCode) // ✅ 코드로 조회
                .build();

        CountryDTO rDTO = countryService.getCountryByCode(pDTO); // 새 메서드 필요

        if (rDTO.getCountryId() == null) {
            return "fail";
        }

        session.setAttribute("SS_COUNTRY_ID", rDTO.getCountryId().toString());
        log.info("사용자 [{}] → 국가 [{}]({}) 선택 완료", userId, rDTO.getCountryName(), rDTO.getCountryCode());

        return "success";
    }

    /**
     * 홈화면 이동 시 → 세션 + DB 데이터 삭제
     */
    @ResponseBody
    @PostMapping("/cancelNation")
    public String cancelNation(HttpServletRequest request, HttpSession session) throws Exception {

        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
        if (userId.isEmpty()) {
            log.warn("⚠️ 로그인 세션 없음 → 삭제 불가");
            return "login_required";
        }

        // 세션에서 확인
        String countryId = CmmUtil.nvl((String) session.getAttribute("SS_COUNTRY_ID"));
        String countryCode = CmmUtil.nvl(request.getParameter("countryCode"));

        log.info("📌 cancelNation 요청: userId={}, countryId(session)={}, countryCode(param)={}",
                userId, countryId, countryCode);

        // 세션에 없으면 countryCode로 DB 조회
        if (countryId.isEmpty() && !countryCode.isEmpty()) {
            CountryDTO pDTO = CountryDTO.builder()
                    .countryCode(countryCode)
                    .build();

            CountryDTO rDTO = countryService.getCountryByCode(pDTO);
            if (rDTO.getCountryId() != null) {
                countryId = String.valueOf(rDTO.getCountryId());
                log.info("📌 DB 조회 결과 → countryId={}", countryId);
            }
        }

        if (countryId.isEmpty()) {
            log.warn("⚠️ countryId 없음 → 삭제 불가");
            return "fail";
        }

        ContractDTO pDTO = ContractDTO.builder()
                .userId(String.valueOf(userId))      // int → String
                .countryId(String.valueOf(countryId)) // int → String
                .build();

        contractService.deleteContractByUserAndCountry(pDTO);
        log.info("✅ 사용자 [{}] → 국가 [{}] 계약서 삭제 완료", userId, countryId);

        session.removeAttribute("SS_COUNTRY_ID");

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

