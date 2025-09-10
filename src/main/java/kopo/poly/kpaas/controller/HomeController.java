package kopo.poly.kpaas.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // "/" 또는 "/index" 요청 시 index.jsp 반환
    @GetMapping({"/", "/index"})
    public String index(HttpSession session, Model model) {

        // 세션에 loginUser 정보가 있으면 JSP에서 바로 EL로 접근 가능
        Object loginId = session.getAttribute("LOGIN_USER_ID");
        Object loginName = session.getAttribute("LOGIN_USER_NAME");
        Object loginNickname = session.getAttribute("LOGIN_USER_NICKNAME");

        // 선택적으로 Model에 넣어도 JSP에서 EL로 접근 가능
        model.addAttribute("loginUserId", loginId);
        model.addAttribute("loginUserName", loginName);
        model.addAttribute("loginUserNickname", loginNickname);

        return "index"; // WEB-INF/views/index.jsp
    }
}

