package kopo.poly.kpaas.controller;

import jakarta.servlet.http.HttpSession;
import kopo.poly.kpaas.dto.ContractDTO;
import kopo.poly.kpaas.service.IAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/contract")
public class ContractViewController {

    private final IAnalysisService analysisService;

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


    @PostMapping("/analyzeSample")
    @ResponseBody
    public String analyzeSampleContract(HttpSession session) {
        try {
            // ✅ 샘플 DTO (DB에 미리 넣은 contract_id=4, user_id=5, country_id=2)
            ContractDTO dto = ContractDTO.builder()
                    .contractId(4L)   // contracts 테이블 contract_id
                    .userId(6L)       // contracts 테이블 user_id
                    .countryId(2L)    // contracts 테이블 country_id
                    .build();

            // 서비스 호출
            analysisService.analyzeContract(dto);

            // ✅ DB 저장까지 끝나면 성공 응답 반환
            return "success";
        } catch (Exception e) {
            log.error("분석 실패", e);
            return "fail";
        }
    }
}
