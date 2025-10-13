package kopo.poly.kpaas.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.kpaas.dto.QaLogDTO;
import kopo.poly.kpaas.service.IChatbotService;
import kopo.poly.kpaas.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor // ✅ final 필드를 자동 생성자 주입
@Controller
@RequestMapping("/chatbot")
public class ChatbotViewController {

    private final IChatbotService chatbotService; //

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

    /**
     * Q&A 챗봇 질문 처리 (AJAX 요청)
     */
    @ResponseBody
    @PostMapping("/ask")
    public String askQuestion(HttpServletRequest request, HttpSession session) throws Exception {

        String userIdStr = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
        int userId = userIdStr.isEmpty() ? 0 : Integer.parseInt(userIdStr);

        String question = CmmUtil.nvl(request.getParameter("question"));

        QaLogDTO pDTO = QaLogDTO.builder()
                .userId(userId)
                .question(question)
                .build();

        String answer = chatbotService.askQuestion(pDTO);

        log.info("질문='{}', 답변='{}'", question, answer);

        return answer;
    }
}
