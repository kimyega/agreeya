package kopo.poly.kpaas.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.kpaas.dto.CaseDTO;
import kopo.poly.kpaas.dto.ContractDTO;
import kopo.poly.kpaas.dto.CountryDTO;
import kopo.poly.kpaas.service.ICaseService;
import kopo.poly.kpaas.service.IContractService;
import kopo.poly.kpaas.service.ICountryService;
import kopo.poly.kpaas.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/contract")
public class ContractViewController {
    private final ICountryService countryService;
    private final IContractService contractService; // ✅ 서비스 주입
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
    @GetMapping("/result")
    public String result(HttpServletRequest request, Model model) {
        // 파라미터에서 contractId 가져오기
        String contractId = CmmUtil.nvl(request.getParameter("contractId"));

        // JSP에서 ${contractId} 로 쓸 수 있도록 모델에 담기
        model.addAttribute("contractId", contractId);

        log.info("📄 계약서 분석 결과 화면 호출 contractId={}", contractId);

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
                .userId(Integer.parseInt(userId))
                .countryId(Integer.parseInt(countryId))
                .build();

        contractService.deleteContractByUserAndCountry(pDTO);
        log.info("✅ 사용자 [{}] → 국가 [{}] 계약서 삭제 완료", userId, countryId);

        session.removeAttribute("SS_COUNTRY_ID");

        return "success";
    }
    // 데이터 조회
    @ResponseBody
    @PostMapping("/similar/data")
    public List<CaseDTO> getSimilarCases(HttpServletRequest request, HttpSession session) throws Exception {
        String contractId = CmmUtil.nvl(request.getParameter("contractId"));
        String countryId = CmmUtil.nvl((String) session.getAttribute("SS_COUNTRY_ID"));

        log.info("▶ 유사사례 조회 contractId={} countryId={}", contractId, countryId);

        CaseDTO pDTO = new CaseDTO();
        pDTO.setContractId(contractId);
        pDTO.setCountryId(countryId);

        return Optional.ofNullable(caseService.getSimilarCases(pDTO))
                .orElseGet(Collections::emptyList);
    }

}
