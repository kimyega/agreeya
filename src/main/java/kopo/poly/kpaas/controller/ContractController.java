package kopo.poly.kpaas.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.kpaas.dto.ContractDTO;
import kopo.poly.kpaas.dto.ContractUploadDTO;
import kopo.poly.kpaas.dto.ResultDTO;
import kopo.poly.kpaas.service.IContractService;
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
        try {
            // 1. Multipart 캐스팅
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

            // 2. 파일 및 userId 가져오기
            MultipartFile file = multipartRequest.getFile("file");
            String userId = multipartRequest.getParameter("userId");

            if (file == null || file.isEmpty()) {
                return ResultDTO.builder()
                        .result(0)
                        .msg("파일이 업로드되지 않았습니다.")
                        .build();
            }

            // 3. ContractUploadDTO 생성
            ContractUploadDTO uploadDTO = new ContractUploadDTO();
            uploadDTO.setFile(file);
            uploadDTO.setUserId(userId);

            // 4. 파일 저장
            String fileUrl = contractService.saveFile(uploadDTO);

            // 5. OCR 실행
            String ocrText = contractService.extractTextFromImage(uploadDTO);

            // 6. ContractDTO 생성 후 세션 저장
            ContractDTO dto = ContractDTO.builder()
                    .userId(userId)
                    .originalFileUrl(fileUrl)
                    .ocrText(ocrText)
                    .build();
            session.setAttribute("contractDraft", dto);

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
    public ResponseEntity<ResultDTO> saveCountry(HttpServletRequest request,
                                                 HttpSession session) {

        // 1. 요청에서 countryId 가져오기
        String countryId = request.getParameter("countryId");

        // 2. 세션에서 contractDraft 가져오기
        ContractDTO dto = (ContractDTO) session.getAttribute("contractDraft");

        if (dto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResultDTO.builder()
                            .result(0)
                            .msg("세션에 계약서 초안이 없습니다. 먼저 업로드하세요.")
                            .build());
        }

        try {
            // 3. countryId 세팅
            dto.setCountryId(countryId);

            // 4. DB 저장
            contractService.saveContract(dto);

            // 5. 세션 정리
            session.removeAttribute("contractDraft");

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

}
