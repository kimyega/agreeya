package kopo.poly.kpaas.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/") // 기본 루트
@Controller
public class ViewTestController {

    // 헬스 체크 / 핑 테스트
    @GetMapping({"", "index"})
    public String index(Model model) {
        log.info("GET / or /index");
        model.addAttribute("msg", "서버 OK - JSP 연결 확인");
        // /WEB-INF/views/common/index.jsp
        return "index";
    }

    // ===== 사용자 영역 =====
    @GetMapping("login")
    public String login() {
        log.info("GET /login");
        return "user/login"; // /WEB-INF/views/user/login.jsp
    }

    @GetMapping("signup")
    public String signup() {
        log.info("GET /signup");
        return "user/signup";
    }

    @GetMapping("findPw")
    public String findPw() {
        log.info("GET /findPw");
        return "user/findPw";
    }

    @GetMapping("changePw")
    public String changePw() {
        log.info("GET /changePw");
        return "user/changePw";
    }

    @GetMapping("findEmail")
    public String findEmail() {
        log.info("GET /findEmail");
        return "user/findEmail";
    }

    @GetMapping("emailVerify")
    public String emailVerify() {
        log.info("GET /emailVerify");
        return "user/emailVerify";
    }

    @GetMapping("phoneVerify")
    public String phoneVerify() {
        log.info("GET /phoneVerify");
        return "user/phoneVerify";
    }



    @GetMapping("mypage")
    public String mypage() {
        log.info("GET /mypage");
        return "user/mypage";
    }

    // ===== 계약서/분석 영역 =====
    @GetMapping("contract/upload")
    public String upload() {
        log.info("GET /contract/upload");
        return "contract/upload";
    }

    @GetMapping("contract/loading")
    public String loading() {
        log.info("GET /contract/loading");
        return "contract/loading";
    }

    @GetMapping("contract/result")
    public String result() {
        log.info("GET /contract/result");
        return "contract/result";
    }

    @GetMapping("contract/similar")
    public String similarCase() {
        log.info("GET /contract/similar");
        return "contract/similarCase";
    }

    @GetMapping("contract/country")
    public String country() {
        log.info("GET /contract/country");
        return "contract/country";
    }

    @GetMapping("contract/draft")
    public String aiContractDraft() {
        log.info("GET /contract/draft");
        return "contract/aiContract";
    }

    // ===== 협상/챗봇 영역 =====
    @GetMapping("chatbot/aiSimulationMain")
    public String aiSimulationMain() {
        log.info("GET /chatbot/aiSimulationMain");
        return "chatbot/aiSimulationMain";
    }

    @GetMapping("chatbot/aiSimulationChat")
    public String aiSimulationChat() {
        log.info("GET /chatbot/aiSimulationChat");
        return "chatbot/aiSimulationChat";
    }

    @GetMapping("chatbot/qnaChatbot")
    public String qnaChatbot() {
        log.info("GET /chatbot/qnaChatbot");
        return "chatbot/qnaChatbot";
    }
}
