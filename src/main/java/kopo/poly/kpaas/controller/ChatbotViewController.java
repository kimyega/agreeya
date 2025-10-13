package kopo.poly.kpaas.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.kpaas.dto.QaLogDTO;
import kopo.poly.kpaas.service.IChatbotService;
import kopo.poly.kpaas.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.kpaas.dto.ContractDTO;
import kopo.poly.kpaas.dto.NegotiationDTO;
import kopo.poly.kpaas.dto.ResultDTO;
import kopo.poly.kpaas.dto.UserDTO;
import kopo.poly.kpaas.service.IContractService;
import kopo.poly.kpaas.service.impl.GptService;
import kopo.poly.kpaas.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor // ✅ final 필드를 자동 생성자 주입
@Controller
@RequestMapping("/chatbot")
public class ChatbotViewController {

    private final IChatbotService chatbotService; //

    private final GptService gptService;
    private final IContractService contractService;

    /**
     * AI 모의 협상 메인 화면
     * -> /WEB-INF/views/chatbot/aiSimulationMain.jsp
     */
    @GetMapping("/aiSimulationMain")
    public String aiSimulationMain(Model model, HttpSession session) throws Exception {
        log.info("🤖 AI 모의 협상 화면 호출");

        String userId = session.getAttribute("SS_USER_ID").toString();

        UserDTO pDTO = UserDTO.builder()
                .userId(userId)
                .build();

        List<ContractDTO> contracts = contractService.getContractListByUserId(pDTO);

        for (ContractDTO c : contracts) {
            // String -> Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date createdAtDate = sdf.parse(c.getCreatedAt());
            c.setCreatedAtDate(createdAtDate);
        }

        model.addAttribute("contracts", contracts);

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

    /**
     * ai 시뮬레이션 챗봇 화면
     * -> /WEB-INF/views/chatbot/aiSimulationChat.jsp
     */
    @GetMapping("/aiSimulationChat")
    public String aiSimulationChat() {
        log.info("🤖 Q&A 챗봇 화면 호출");
        return "chatbot/aiSimulationChat";
    }


    @PostMapping("/simulate")
    @ResponseBody
    public ResultDTO simulateNegotiation(HttpServletRequest request, HttpSession session) {

        String contractIdStr = CmmUtil.nvl(request.getParameter("contractId"));
        Integer contractId = null;
        try {
            if (!contractIdStr.isEmpty()) {
                contractId = Integer.valueOf(contractIdStr);
            }
        } catch (NumberFormatException e) {
            log.warn("contractId 변환 실패: {}", contractIdStr);
        }

        // 요청 데이터
        String situation = request.getParameter("situation");
        String country = request.getParameter("country");
        String position = request.getParameter("position");
        String goal = request.getParameter("goal");

        log.info("🧩 AI 협상 요청 - contractId: {}, situation: {}, country: {}, position: {}, goal: {}",
                contractId, situation, country, position, goal);

        // DTO 생성 후 조건에 따라 값 세팅
        NegotiationDTO negotiation = new NegotiationDTO();
        if (contractId != null) {
            negotiation.setContractId(contractId);
            // contractId가 있으면, 계약서 관련 정보만 필요
        } else {
            negotiation.setSituation(situation);
            negotiation.setCountry(country);
            negotiation.setPosition(position);
            negotiation.setGoal(goal);
        }

        // 세션에 한 번만 저장
        session.setAttribute("SS_NEGOTIATION_DATA", negotiation);
        log.info("✅ 세션에 SS_NEGOTIATION_DATA 저장 완료");

        return ResultDTO.builder()
                .result(1)
                .msg("협상 시뮬레이션 준비 완료")
                .data(null)
                .build();
    }


    @PostMapping("/simulateChat")
    @ResponseBody
    public ResultDTO simulateChat(HttpServletRequest request, HttpSession session) {

        // request에서 파라미터 꺼내기
        String scenarioInput = request.getParameter("scenarioInput");
        log.info("📝 사용자 입력 메시지: {}", scenarioInput);

        if (scenarioInput == null || scenarioInput.trim().isEmpty()) {
            return ResultDTO.builder()
                    .result(0)
                    .msg("입력값이 없습니다.")
                    .data(null)
                    .build();
        }

        // 세션에서 기존 협상 데이터 가져오기
        NegotiationDTO negotiation = (NegotiationDTO) session.getAttribute("SS_NEGOTIATION_DATA");
        if (negotiation == null) {
            log.warn("❌ 세션에 협상 데이터가 없습니다.");
            return ResultDTO.builder()
                    .result(0)
                    .msg("세션에 협상 데이터가 없습니다.")
                    .data(null)
                    .build();
        }

        // GPT 서비스 호출 (세션 데이터 + 사용자 메시지)
        String aiReply = simulateNegotiation(negotiation, scenarioInput);

        log.info("🤖 AI 응답: {}", aiReply);

        return ResultDTO.builder()
                .result(1)
                .msg("성공")
                .data(aiReply)
                .build();
    }

    /**
     * AI 협상 시뮬레이션
     * @param negotiation 세션에 저장된 협상 데이터
     * @param userMessage 사용자 입력 메시지
     * @return GPT 생성 응답
     */
    public String simulateNegotiation(NegotiationDTO negotiation, String userMessage) {
        try {
            StringBuilder prompt = new StringBuilder();


            // 계약 관련 정보가 있는 경우
            if (negotiation.getContractId() != null) {
                String contractId = negotiation.getContractId() + "";
                ContractDTO pDTO = ContractDTO.builder()
                        .contractId(contractId)
                        .build();
                prompt.append("계약서 내용: ").append(negotiation.getContractId()).append("\n")
                        .append(contractService.getContractById(pDTO).getOcrText());
            } else {
                // 계약 ID 없으면 상황, 국가, 직위, 목표 등 일반 협상 정보 추가
                if (negotiation.getSituation() != null) {
                    prompt.append("상황: ").append(negotiation.getSituation()).append("\n");
                }
                if (negotiation.getCountry() != null) {
                    prompt.append("국가: ").append(negotiation.getCountry()).append("\n");
                }
                if (negotiation.getPosition() != null) {
                    prompt.append("직위: ").append(negotiation.getPosition()).append("\n");
                }
                if (negotiation.getGoal() != null) {
                    prompt.append("목표: ").append(negotiation.getGoal()).append("\n");
                }
            }

            // 공통: 사용자 메시지와 역할 지시
            prompt.append("사용자의 요청: ").append(userMessage).append("\n");
            prompt.append("계약 조건을 고려하여 AI 고용주 역할로 답변해주세요.");

            log.info("🧠 GPT simulateNegotiation 프롬프트: {}", prompt.toString());

            // 기존 generateText 사용
            return gptService.generateText(prompt.toString());

        } catch (Exception e) {
            log.error("simulateNegotiation 오류", e);
            return "AI 협상 응답 생성 중 오류가 발생했습니다.";
        }
    }
}
