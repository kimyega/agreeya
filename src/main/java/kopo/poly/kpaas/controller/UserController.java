package kopo.poly.kpaas.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/") // 기본 루트
@Controller
public class UserController {


    // ===== 사용자 영역 =====
    @GetMapping("login")
    public String login() {
        log.info("GET /login");
        return "user/login"; // /WEB-INF/views/user/login.jsp
    }

    @GetMapping("signup")
    public String signup() {
        log.info("GET /signup");
        return "user/signup";
    }

    @GetMapping("findEmail")
    public String findEmail() {
        log.info("GET /findEmail");
        return "user/findEmail";
    }

    @GetMapping("findPw")
    public String findPw() {
        log.info("GET /findPw");
        return "user/findPw";
    }

    @GetMapping("phoneVerify")
    public String phoneVerify() {
        log.info("GET /phoneVerify");
        return "user/phoneVerify";
    }

    @GetMapping("emailVerify")
    public String emailVerify() {
        log.info("GET /emailVerify");
        return "user/emailVerify";
    }

    @GetMapping("changePw")
    public String changePw() {
        log.info("GET /changePw");
        return "user/changePw";
    }



}

