package kopo.poly.kpaas.service.impl;

import kopo.poly.kpaas.dto.QaLogDTO;
import kopo.poly.kpaas.mapper.IChatbotMapper;
import kopo.poly.kpaas.service.IChatbotService;
import kopo.poly.kpaas.util.CmmUtil;
import kopo.poly.kpaas.util.OpenAiChatUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatbotService implements IChatbotService {

    private final IChatbotMapper chatbotMapper;
    private final OpenAiChatUtil openAiChatUtil; // ✅ GPT Q&A 유틸

    @Override
    public String askQuestion(QaLogDTO pDTO) throws Exception {

        String question = CmmUtil.nvl(pDTO.getQuestion());
        if (question.isEmpty()) {
            return "⚠️ 질문이 비어있습니다.";
        }

        // ✅ GPT 호출
        String answer = openAiChatUtil.askLaborLaw(question);

        // ✅ DB 저장
        pDTO.setAnswer(answer);
        chatbotMapper.insertQaLog(pDTO);

        log.info("질문 저장 완료: {}", question);

        return answer;
    }
}
