package kopo.poly.kpaas.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/") // 루트 전용
public class ViewTestController {

    // 헬스 체크 / 메인
    @GetMapping({"", "index"})
    public String index(Model model) {
        log.info("GET / or /index");
        model.addAttribute("msg", "서버 OK - JSP 연결 확인");

        return "index"; // /WEB-INF/views/index.jsp
    }

//
//    @GetMapping("mypage")
//    public String mypage() {
//        log.info("GET /mypage");
//        return "user/mypage";
//    }
//
//    // ===== 계약서/분석 영역 =====
//    @GetMapping("contract/upload")
//    public String upload() {
//        log.info("GET /contract/upload");
//        return "contract/upload";
//    }
//
//    @GetMapping("contract/loading")
//    public String loading() {
//        log.info("GET /contract/loading");
//        return "contract/loading";
//    }
//
//
//    @GetMapping("contract/similar")
//    public String similarCase() {
//        log.info("GET /contract/similar");
//        return "contract/similarCase";
//    }
//
//
//    @GetMapping("contract/draft")
//    public String aiContractDraft() {
//        log.info("GET /contract/draft");
//        return "contract/aiContract";
//    }
//
//    // ===== 협상/챗봇 영역 =====
//    @GetMapping("chatbot/aiSimulationMain")
//    public String aiSimulationMain() {
//        log.info("GET /chatbot/aiSimulationMain");
//        return "chatbot/aiSimulationMain";
//    }
//
//    @GetMapping("chatbot/aiSimulationChat")
//    public String aiSimulationChat() {
//        log.info("GET /chatbot/aiSimulationChat");
//        return "chatbot/aiSimulationChat";
//    }
//
//    @GetMapping("chatbot/qnaChatbot")
//    public String qnaChatbot() {
//        log.info("GET /chatbot/qnaChatbot");
//        return "chatbot/qnaChatbot";
//    }
}
