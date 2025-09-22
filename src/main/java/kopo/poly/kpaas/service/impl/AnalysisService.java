package kopo.poly.kpaas.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.kpaas.dto.ContractDTO;
import kopo.poly.kpaas.dto.ContractClauseDTO;
import kopo.poly.kpaas.dto.ContractAnalysisSummaryDTO;
import kopo.poly.kpaas.dto.LawDTO;
import kopo.poly.kpaas.mapper.IAnalysisMapper;
import kopo.poly.kpaas.service.IAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisService implements IAnalysisService {

    private final IAnalysisMapper analysisMapper;
    private final GptService gptService; // GPT API 호출용

    @Override
    public void analyzeContract(ContractDTO pDTO) throws Exception {

        Integer contractId = Integer.parseInt(pDTO.getContractId());
        Integer countryId = Integer.parseInt(pDTO.getCountryId());
        String userId = pDTO.getUserId();

        log.info("userId: {}, contractId: {}, countryId: {}", userId, contractId, countryId);

        // 1. 계약서 본문 조회
        ContractDTO rDTO = analysisMapper.getContractById(pDTO)
                .orElseThrow(() -> new Exception("계약서가 존재하지 않습니다"));

        // 2. 국가별 법령 조회
        LawDTO lDTO = new LawDTO();
        lDTO.setCountryId(countryId);
        LawDTO lawDTO = analysisMapper.getLawByCountryId(lDTO);
        if (lawDTO == null) {
            log.error("해당 국가 법령이 없습니다. countryId={}", countryId);
            return;
        } else if (lawDTO.getCountryId() != countryId) {
            log.error("다른 국가의 법령을 조회하였습니다.={}", countryId);
            return;
        }

        // 3. GPT 프롬프트 생성
        String prompt =
                "다음은 계약서 본문과 해당 국가의 법령입니다.\n\n" +
                        "계약서 본문:\n" + rDTO.getOcrText() + "\n\n" +
                        "관련 법령:\n" + lawDTO.getContent() + "\n\n" +
                        "👉 계약서와 법령을 비교하여 위험 요소를 분석해줘.\n" +
                        "- 계약서 조항별로 위험/주의/안전 여부 분류\n" +
                        "- 결과를 JSON 형식으로 반환\n" +
                        "[\n" +
                        " {\"clause\": \"조항 내용\", \"result\": \"위험/주의/안전\", \"comment\": \"설명\"},\n" +
                        " ...\n" +
                        "]\n" +
                        "또한 계약서 전체 요약 분석도 별도로 JSON으로 추가해줘.\n";

        log.info("GPT 프롬프트: {}", prompt);

        // 4. GPT 호출
        String gptResult = gptService.generateText(prompt);
        log.info("GPT 응답 결과 : {}", gptResult);

        // 5. JSON 파싱
        ObjectMapper mapper = new ObjectMapper();

        try {
            // (1) 조항별 분석 결과
            List<ContractClauseDTO> clauseList = mapper.readValue(
                    gptResult,
                    new TypeReference<>() {}
            );

            for (ContractClauseDTO clause : clauseList) {
                clause.setContractId(contractId.toString());
                analysisMapper.insertClauseResult(clause);
            }

            // (2) 계약서 전체 요약 저장
            analysisMapper.insertSummaryResult(ContractAnalysisSummaryDTO.builder()
                    .summaryId(contractId)
                    .translatedText(gptResult)
                    .build());

            log.info("분석 결과 저장 완료");

        } catch (Exception e) {
            log.error("GPT 응답 JSON 파싱 실패: {}", e.getMessage());
            throw e;
        }
    }
}
