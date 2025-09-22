package kopo.poly.kpaas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import kopo.poly.kpaas.dto.ContractAnalysisSummaryDTO;
import kopo.poly.kpaas.dto.ContractClauseDTO;
import kopo.poly.kpaas.dto.ContractDTO;
import kopo.poly.kpaas.dto.LawDTO;
import kopo.poly.kpaas.service.IAnalysisService;
import kopo.poly.kpaas.service.impl.GptService;
import kopo.poly.kpaas.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.kpaas.util.CmmUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @ResponseBody
    @PostMapping("/selectNation")
    public String selectNation(HttpServletRequest request, HttpSession session) {
        log.info("{}.selectNation Start!", this.getClass().getName());
        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
        if (userId.isEmpty()) {
            log.warn("⚠️ 로그인 세션 없음 → 국가 선택 불가");
            return "login_required";
        }

        String countryId = CmmUtil.nvl(request.getParameter("countryId"));
        log.info("사용자 [{}] → 선택 국가 [{}]", userId, countryId);

        // 👉 DB에는 저장하지 않고 세션에만 유지
        session.setAttribute("SELECTED_COUNTRY_ID", countryId);
        log.info("{}.selectNation End!", this.getClass().getName());

        return "success";
    }

    /**
     * 국가 선택 취소 → 세션 삭제
     */
    @ResponseBody
    @PostMapping("/cancelNation")
    public String cancelNation(HttpSession session) {
        session.removeAttribute("SELECTED_COUNTRY_ID");
        log.info("국가 선택 세션값 제거 완료"); //DB에 정보 지우는거(홈화면으로 나가면,세션+DB저장값(샘플))
        return "success";
    }


    @GetMapping("/draft")
    public String draft() {
        log.info("📄 AI 계약서 초안 화면 호출");
        return "contract/aiContract"; // → contract/aiContract.jsp
    }
    @PostMapping("/analyze")
    @ResponseBody
    public String analyzeContract(HttpServletRequest request, HttpSession session) throws Exception {

        log.info("{}.analyzeContract Start!!", this.getClass().getSimpleName());

        ContractDTO pDTO = ContractDTO.builder()
                .contractId(CmmUtil.nvl(request.getParameter("contractId")))
                .countryId(CmmUtil.nvl(session.getAttribute("SELECTED_COUNTRY_ID").toString()))
                .userId(CmmUtil.nvl(session.getAttribute("SS_USER_ID").toString()))
                .build();

        analysisService.analyzeContract(pDTO);

        log.info("{}.analyzeContract End!!", this.getClass().getSimpleName());

        return "분석 완료";
    }



}
