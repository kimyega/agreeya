package kopo.poly.kpaas.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.kpaas.service.ILawService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/law")
public class LawController {

    private final ILawService lawService;

    @GetMapping("/uploadLaborLaws")
    @ResponseBody
    public String uploadLaborLaws(HttpServletRequest request, HttpSession session) throws Exception {

        log.info("uploadLaborLaws Start!");

        // resources/xml/law.xml 폴더 불러오기
        ClassPathResource resource = new ClassPathResource("xml/law.xml");
        File parentDir = resource.getFile();

        int countryId = 1; // 일본 (countries 테이블에 등록된 ID)
        int totalInserted = 0;
        int fileCount = 0;

        if (parentDir.exists() && parentDir.isDirectory()) { //실제로 파일이 존재하는지 확인&&폴더인지 확인
            for (File xmlFile : parentDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"))) {
                //람다식 반복문으로 .xml로 끝나는 파일을 찾는다
                //dir:부모디렉토리,name:파일이름
                log.info("Processing file: {}", xmlFile.getName());

                int res = lawService.parseXmlAndInsert(xmlFile, countryId, request);

                totalInserted += res; //res:DB에 저장한 법률 조항 계수
                fileCount++;
            }
        } else {
            return "❌ law.xml 폴더를 찾을 수 없습니다.";
        }

        log.info("uploadLaborLaws End! Files={}, Total Inserted={}", fileCount, totalInserted);

        return "✅ Labor Laws Insert Completed! Files=" + fileCount + ", Total Inserted=" + totalInserted;
    }

}
