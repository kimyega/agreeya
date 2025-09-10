package kopo.poly.kpaas.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/contract")
public class ContractViewController {

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
}
