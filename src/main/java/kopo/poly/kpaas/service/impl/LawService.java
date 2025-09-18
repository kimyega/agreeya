package kopo.poly.kpaas.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import kopo.poly.kpaas.dto.LawDTO;
import kopo.poly.kpaas.mapper.ILawMapper;
import kopo.poly.kpaas.service.ILawService;
import kopo.poly.kpaas.util.CmmUtil;
import kopo.poly.kpaas.util.OpenAiEmbeddingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class LawService implements ILawService {

    private final ILawMapper lawMapper;
    private final OpenAiEmbeddingUtil embeddingUtil; // ✅ 주입

    @Override
    public int bulkInsertLaws(List<LawDTO> pList, HttpServletRequest request) throws Exception {
        log.info("bulkInsertLaws Start!");
        int res = 0;

        for (LawDTO pDTO : pList) { //pDTO라는 이름에 pList저장
            if (pDTO == null) continue;

            pDTO.setTitle(CmmUtil.nvl(pDTO.getTitle()));
            pDTO.setArticleNumber(CmmUtil.nvl(pDTO.getArticleNumber()));
            pDTO.setContent(CmmUtil.nvl(pDTO.getContent()));
            pDTO.setUpdatedAt(CmmUtil.nvl(pDTO.getUpdatedAt()));
            //CmmUtil에 적용 ->안전하게 널 처리
            /** ✅ 임베딩 생성 후 JSON으로 저장 */
            String textForEmbedding = pDTO.getTitle() + " "
                    + pDTO.getArticleNumber() + " "
                    + pDTO.getContent();

            List<Float> vector = embeddingUtil.createEmbedding(textForEmbedding); // OpenAI 호출
            String vectorJson = new ObjectMapper().writeValueAsString(vector); //Json문자열로 리턴
            pDTO.setLawVector(vectorJson); //pDTO에 저장

            res += lawMapper.insertLaw(pDTO);
        }

        log.info("bulkInsertLaws End! Insert Count={}", res);
        return res;
    }

    /** ✅ XML 파일 파싱 후 DB Insert */
    @Override
    public int parseXmlAndInsert(File xmlFile, int countryId, HttpServletRequest request) throws Exception {

        log.info("parseXmlAndInsert Start! file={}", xmlFile.getName());

        List<LawDTO> lawList = new ArrayList<>();

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        // 법령명 (LawTitle)
        String lawTitle = "";
        NodeList lawTitleList = doc.getElementsByTagName("LawTitle");
        if (lawTitleList.getLength() > 0) {
            lawTitle = lawTitleList.item(0).getTextContent();
        }

        // 조문(Article),xml구조별 텍스트를 반복문으로 돌려서 DTO객체화
        NodeList articleList = doc.getElementsByTagName("Article");
        for (int i = 0; i < articleList.getLength(); i++) {
            Element articleEl = (Element) articleList.item(i);

            String articleNum = "";
            StringBuilder contentBuilder = new StringBuilder();

            NodeList articleTitleList = articleEl.getElementsByTagName("ArticleTitle");
            if (articleTitleList.getLength() > 0) {
                articleNum = articleTitleList.item(0).getTextContent();
            }

            NodeList sentenceList = articleEl.getElementsByTagName("Sentence");
            for (int j = 0; j < sentenceList.getLength(); j++) {
                contentBuilder.append(sentenceList.item(j).getTextContent()).append("\n");
            }

            LawDTO dto = LawDTO.builder()
                    .countryId(countryId)
                    .title(lawTitle)
                    .articleNumber(articleNum)
                    .content(contentBuilder.toString().trim())
                    .updatedAt("") // XML에 날짜 없으면 빈 값
                    .build();

            lawList.add(dto);
        }

        int res = bulkInsertLaws(lawList, request);

        log.info("parseXmlAndInsert End! Insert Count={}", res);
        return res;
    }
}
