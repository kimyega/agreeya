package kopo.poly.kpaas.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserViewController {

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "user/signup";
    }

    @GetMapping("/findPw")
    public String findPw() {
        return "user/findPw";
    }

    @GetMapping("/findEmail")
    public String findEmail() {
        return "user/findEmail";
    }

    @GetMapping("/emailVerify")
    public String emailVerify() {
        return "user/emailVerify";
    }

    @GetMapping("/phoneVerify")
    public String phoneVerify() {
        return "user/phoneVerify";
    }

}
