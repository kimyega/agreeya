package kopo.poly.kpaas.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kopo.poly.kpaas.dto.ContractClauseDTO;
import kopo.poly.kpaas.dto.ContractAnalysisSummaryDTO;
import kopo.poly.kpaas.dto.ContractDTO;
import kopo.poly.kpaas.dto.DraftDTO;
import kopo.poly.kpaas.mapper.IDraftMapper;
import kopo.poly.kpaas.service.IDraftService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

        // 1️⃣ DB 조회
        List<ContractClauseDTO> clauses = getClausesByContractId(pDTO);
        ContractAnalysisSummaryDTO summary = getSummaryByContractId(pDTO);

        if (clauses == null || clauses.isEmpty() || summary == null) {
            throw new Exception("❌ 분석 결과 없음: contractId=" + pDTO.getContractId());
        }

        // 2️⃣ 프롬프트 생성용 텍스트 구성
        StringBuilder clauseText = new StringBuilder();
        for (ContractClauseDTO c : clauses) {
            clauseText.append("- ").append(c.getClauseText())
                    .append(" (").append(c.getAiComment()).append(")")
                    .append("\n");
        }

        // ✅ 수정된 프롬프트 시작 (다국어 JSON 구조 / 주소 제거 / 조항 개수 유연화 반영)
        String prompt = """
                아래는 계약서 분석 요약과 조항별 AI 코멘트입니다.
                
                [요약]
                %s
                
                [조항별 코멘트]
                %s
                
                위 내용을 반영하여 개선된 근로계약서를 
                한국어(KR), 영어(EN), 일본어(JP) 버전으로 각각 작성해 주세요.
                ⚠️ 반드시 JSON 형식으로만 출력하고, JSON 외의 다른 텍스트는 포함하지 마세요.
                
                JSON 구조는 아래와 같습니다:
                
                {
                  "draftKr": {
                    "employer": {
                      "companyName": "회사명",
                      "representative": "대표자명",
                      "businessType": "업종"
                    },
                    "employee": {
                      "name": "근로자명"
                    },
                    "clauses": [
                      "제1조 (근무장소): ...",
                      "제2조 (업무내용): ...",
                      "제3조 (임금): ...",
                      ...
                    ],
                    "date": "YYYY년 M월 D일"
                  },
                  "draftEn": {
                    "employer": {
                      "companyName": "Company Name",
                      "representative": "Representative",
                      "businessType": "Business Type"
                    },
                    "employee": {
                      "name": "Employee Name"
                    },
                    "clauses": [
                      "Article 1 (Workplace): ...",
                      "Article 2 (Duties): ...",
                      "Article 3 (Wage): ...",
                      ...
                    ],
                    "date": "YYYY-MM-DD"
                  },
                  "draftJp": {
                    "employer": {
                      "companyName": "会社名",
                      "representative": "代表者",
                      "businessType": "業種"
                    },
                    "employee": {
                      "name": "従業員氏名"
                    },
                    "clauses": [
                      "第1条 (勤務場所): ...",
                      "第2条 (業務内容): ...",
                      "第3条 (賃金): ...",
                      ...
                    ],
                    "date": "YYYY年M月D日"
                  }
                }
                
                규칙:
                - clauses의 개수는 계약 내용에 맞게 자유롭게 작성 (5개 이하 또는 10개 이상 가능)
                - 각 언어의 date 필드는 반드시 오늘 날짜를 포함해야 하며, 서버 기준의 현재 날짜를 사용한다.
                - 한국어: "YYYY년 MM월 DD일", 영어: "YYYY-MM-DD", 일본어: "YYYY年MM月DD日" 형식으로 작성한다.
                - JSON 이외의 텍스트는 절대 출력하지 말라.
                """.formatted(summary.getTranslatedText(), clauseText);
        // ✅ 프롬프트 끝

        // 3️⃣ GPT 호출
        String gptResponse = gptService.generateText(prompt);
        log.debug("🤖 GPT Draft Response: {}", gptResponse);

        // 4️⃣ JSON 파싱
        String cleaned = gptResponse.replace("```json", "").replace("```", "").trim();
        JsonNode root = mapper.readTree(cleaned);

        JsonNode draftKr = root.path("draftKr");
        JsonNode draftEn = root.path("draftEn");
        JsonNode draftJp = root.path("draftJp");

        // ✅ 오늘 날짜 생성
        String todayKr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
        String todayEn = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String todayJp = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));

        // ✅ GPT 날짜 무조건 덮어쓰기 (랜덤 방지)
        ((ObjectNode) draftKr).put("date", todayKr);
        ((ObjectNode) draftEn).put("date", todayEn);
        ((ObjectNode) draftJp).put("date", todayJp);

        // ✅ 수정된 JSON 다시 DraftDTO로 변환
        return DraftDTO.builder()
                .draftKr(draftKr.toString())
                .draftEn(draftEn.toString())
                .draftJp(draftJp.toString())
                .build();

    }
}