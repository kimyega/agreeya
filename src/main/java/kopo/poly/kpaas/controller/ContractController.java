package kopo.poly.kpaas.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.kpaas.dto.ContractDTO;
import kopo.poly.kpaas.dto.ContractUploadDTO;
import kopo.poly.kpaas.dto.ResultDTO;
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

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/contract")
@Controller
public class ContractController {

    private final IContractService contractService;

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

    // 1. 업로드 + OCR 처리
    @PostMapping("/uploadFile")
    @ResponseBody
    public ResultDTO uploadContract(HttpServletRequest request, HttpSession session) {
        log.info("📄 /uploadFile 요청 시작");
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

            MultipartFile file = multipartRequest.getFile("file");
            String userId = multipartRequest.getParameter("userId");

            log.info("사용자 [{}] 파일 업로드 시도: {}", userId, (file != null ? file.getOriginalFilename() : "파일 없음"));

            if (file == null || file.isEmpty()) {
                log.warn("⚠️ 파일이 업로드되지 않음");
                return ResultDTO.builder()
                        .result(0)
                        .msg("파일이 업로드되지 않았습니다.")
                        .build();
            }

            ContractUploadDTO uploadDTO = new ContractUploadDTO();
            uploadDTO.setFile(file);
            uploadDTO.setUserId(userId);

            log.info("파일 저장 시작");
            String fileUrl = contractService.saveFile(uploadDTO);
            log.info("파일 저장 완료 → URL: {}", fileUrl);

            log.info("OCR 실행 시작");
            String ocrText = contractService.extractTextFromImage(uploadDTO);
            log.info("OCR 완료 → 추출 텍스트 길이: {}", ocrText.length());

            ContractDTO dto = ContractDTO.builder()
                    .userId(userId)
                    .originalFileUrl(fileUrl)
                    .ocrText(ocrText)
                    .build();

            session.setAttribute("contractDraft", dto);
            log.info("세션에 contractDraft 저장 완료");

            return ResultDTO.builder()
                    .result(1)
                    .msg("OCR 완료")
                    .data(ocrText)
                    .build();

        } catch (Exception e) {
            log.error("OCR 실패", e);
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
