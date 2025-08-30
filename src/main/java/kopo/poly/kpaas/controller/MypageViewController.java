package kopo.poly.kpaas.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class MypageViewController {

    @GetMapping("/mypage")
    public String mypage(HttpSession session) {
        // 로그인 시 저장한 세션 키와 동일하게 확인해야 합니다.
        if (session.getAttribute("SS_USER_ID") == null) {
            // 자기 자신이 아닌 로그인 페이지로 보냄
            return "redirect:/user/login";
        }
        return "user/mypage"; // 뷰 이름만 반환 (redirect 금지)
    }

    @GetMapping("/changePw")
    public String changePwView(HttpSession s) {
        if (s.getAttribute("LOGIN_USER_ID") == null) return "redirect:/user/login";
        return "user/changePw";
    }

    @GetMapping("/delete")
    public String deleteView(HttpSession s) {
        if (s.getAttribute("LOGIN_USER_ID") == null) return "redirect:/user/login";
        return "user/delete";
    }
}
