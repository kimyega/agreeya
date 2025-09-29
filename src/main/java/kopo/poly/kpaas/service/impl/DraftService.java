package kopo.poly.kpaas.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.kpaas.dto.ContractClauseDTO;
import kopo.poly.kpaas.dto.ContractAnalysisSummaryDTO;
import kopo.poly.kpaas.dto.ContractDTO;
import kopo.poly.kpaas.dto.DraftDTO;
import kopo.poly.kpaas.mapper.IDraftMapper;
import kopo.poly.kpaas.service.IDraftService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DraftService implements IDraftService {

    private final IDraftMapper draftMapper;
    private final GptService gptService;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<ContractClauseDTO> getClausesByContractId(ContractDTO pDTO) throws Exception {
        return draftMapper.getClausesByContractId(pDTO);
    }

    @Override
    public ContractAnalysisSummaryDTO getSummaryByContractId(ContractDTO pDTO) throws Exception {
        return draftMapper.getSummaryByContractId(pDTO);
    }

    @Override
    public DraftDTO generateDraftContract(ContractDTO pDTO) throws Exception {
        // 1. DB 조회
        List<ContractClauseDTO> clauses = getClausesByContractId(pDTO);
        ContractAnalysisSummaryDTO summary = getSummaryByContractId(pDTO);

        if (clauses == null || clauses.isEmpty() || summary == null) {
            throw new Exception("❌ 분석 결과 없음: contractId=" + pDTO.getContractId());
        }

        // 2. 프롬프트 생성
        StringBuilder clauseText = new StringBuilder();
        for (ContractClauseDTO c : clauses) {
            clauseText.append("- ").append(c.getClauseText())
                    .append(" (").append(c.getAiComment()).append(")")
                    .append("\n");
        }

        String prompt = """
        아래는 계약서 분석 요약과 조항별 AI 코멘트입니다.

        [요약]
        %s

        [조항별 코멘트]
        %s

        위 내용을 반영하여 개선된 근로계약서를
        한국어(KR), 영어(EN), 일본어(JP) 버전으로 각각 작성해 주세요.
        반드시 JSON으로만 응답하세요:

        {
          "draftKr": "...",
          "draftEn": "...",
          "draftJp": "..."
        }
        """.formatted(summary.getTranslatedText(), clauseText);

        // 3. GPT 호출
        String gptResponse = gptService.generateText(prompt);
        log.debug("🤖 GPT Draft Response: {}", gptResponse);

        String cleaned = gptResponse.replace("```json", "").replace("```", "").trim();
        JsonNode root = mapper.readTree(cleaned);

        return DraftDTO.builder()
                .draftKr(root.path("draftKr").asText("생성 실패"))
                .draftEn(root.path("draftEn").asText("생성 실패"))
                .draftJp(root.path("draftJp").asText("생성 실패"))
                .build();
    }
}
