package kopo.poly.kpaas.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Profile({"local","dev"}) // 운영 배포 시 비활성
@Controller
public class DevLoginController {

    @GetMapping("/dev/login-as/{userId}")
    public String loginAs(@PathVariable String userId, HttpSession s) {
        s.setAttribute("LOGIN_USER_ID", userId);
        return "redirect:/user/mypage";
    }

    @GetMapping("/dev/logout")
    public String logout(HttpSession s) {
        s.invalidate();
        return "redirect:/user/login";
    }
}
