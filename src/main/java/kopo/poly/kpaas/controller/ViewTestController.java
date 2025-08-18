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
}
