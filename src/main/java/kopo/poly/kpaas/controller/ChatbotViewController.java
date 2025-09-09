package kopo.poly.kpaas.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/chatbot")
public class ChatbotViewController {

    /**
     * AI 모의 협상 메인 화면
     * -> /WEB-INF/views/chatbot/aiSimulationMain.jsp
     */
    @GetMapping("/aiSimulationMain")
    public String aiSimulationMain() {
        log.info("🤖 AI 모의 협상 화면 호출");
        return "chatbot/aiSimulationMain";
    }

    /**
     * Q&A 챗봇 화면
     * -> /WEB-INF/views/chatbot/qnaChatbot.jsp
     */
    @GetMapping("/qnaChatbot")
    public String qnaChatbot() {
        log.info("🤖 Q&A 챗봇 화면 호출");
        return "chatbot/qnaChatbot";
    }
}
