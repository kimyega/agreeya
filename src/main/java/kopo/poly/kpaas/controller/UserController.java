package kopo.poly.kpaas.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.kpaas.dto.UserDTO;
import kopo.poly.kpaas.service.IUserService;
import kopo.poly.kpaas.util.CmmUtil;
import kopo.poly.kpaas.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user") // ✅ user 영역으로 고정
@Controller
public class UserController {

    private final IUserService userService;

    // ===== 로그인 처리 =====
    @ResponseBody
    @PostMapping("/loginProc")
    public UserDTO loginProc(HttpServletRequest request, HttpSession session) throws Exception {
        log.info("🟢 loginProc 실행");

        UserDTO pDTO = new UserDTO();
        pDTO.setEmail(CmmUtil.nvl(request.getParameter("email")));
        pDTO.setPassword(CmmUtil.nvl(request.getParameter("password")));

        UserDTO rDTO = userService.getUserLogin(pDTO);

        if (rDTO != null) {
            session.setAttribute("LOGIN_USER_ID", rDTO.getUserId());
            session.setAttribute("LOGIN_USER_NAME", rDTO.getName());

            log.info("✅ 로그인 성공 - userId={}, name={}", rDTO.getUserId(), rDTO.getName());
            return rDTO;
        }

        log.warn("❌ 로그인 실패 - email={}", pDTO.getEmail());
        return null;
    }

    /**
     * 마이페이지 뷰
     */
    @GetMapping("/mypage")
    public String mypage() {
        log.info("🟢 mypage 진입");
        return "user/mypage";
    }

    /**
     * 마이페이지 프로필 조회
     */
    @ResponseBody
    @GetMapping("/mypage/profile")
    public UserDTO getProfile(HttpSession session) throws Exception {
        log.info("🟢 getProfile 실행");

        String userId = CmmUtil.nvl((String) session.getAttribute("LOGIN_USER_ID"));
        if (userId.isEmpty()) {
            log.warn("⚠️ 세션 없음 → 로그인 필요");
            return null;
        }

        UserDTO pDTO = new UserDTO();
        pDTO.setUserId(userId);

        UserDTO rDTO = userService.getUserProfile(pDTO);
        log.info("📌 프로필 조회 결과: {}", rDTO);

        return rDTO;
    }

    /**
     * 회원 탈퇴
     */
    @ResponseBody
    @PostMapping("/delete")
    public int deleteUser(HttpSession session) throws Exception {
        log.info("🟢 deleteUser 실행");

        String userId = CmmUtil.nvl((String) session.getAttribute("LOGIN_USER_ID"));
        if (userId.isEmpty()) {
            log.warn("⚠️ 로그인 안됨 - 탈퇴 불가");
            return 0;
        }

        UserDTO pDTO = new UserDTO();
        pDTO.setUserId(userId);

        int res = userService.deleteUser(pDTO);

        if (res > 0) {
            session.invalidate();
            log.info("✅ 회원 탈퇴 성공 - userId={}", userId);
        } else {
            log.warn("❌ 회원 탈퇴 실패 - userId={}", userId);
        }

        return res;
    }

    // ===== 로그아웃 =====
    @GetMapping("/logout") // ✅ 중복 제거
    public String logout(HttpSession session) {
        log.info("로그아웃 시작");

        session.invalidate(); // 세션 전체 제거

        log.info("로그아웃 완료 → 메인 페이지로 이동");
        return "redirect:/"; // ✅ 홈으로 리다이렉트
    }


    // ===== 화면 이동 =====
    @GetMapping("/login")
    public String login() {
        log.info("GET /user/login");
        return "user/login";
    }

    @GetMapping("/signup")
    public String signup() {
        log.info("GET /user/signup");
        return "user/signup";
    }

    @GetMapping("/findEmail")
    public String findEmail() {
        log.info("GET /user/findEmail");
        return "user/findEmail";
    }

    @GetMapping("/findPw")
    public String findPw() {
        log.info("GET /user/findPw");
        return "user/findPw";
    }

    @GetMapping("/phoneVerify")
    public String phoneVerify() {
        log.info("GET /user/phoneVerify");
        return "user/phoneVerify";
    }

    @GetMapping("/emailVerify")
    public String emailVerify() {
        log.info("GET /user/emailVerify");
        return "user/emailVerify";
    }

    @GetMapping("/changePw")
    public String changePw() {
        log.info("GET /user/changePw");
        return "user/changePw";
    }

}
