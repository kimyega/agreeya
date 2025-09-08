package kopo.poly.kpaas.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.kpaas.dto.UserDTO;
import kopo.poly.kpaas.service.IUserService;
import kopo.poly.kpaas.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
public class UserController {

    private final IUserService userService;

    // ===== 로그인 처리 =====
    @ResponseBody
    @PostMapping("/loginProc")
    public UserDTO loginProc(HttpServletRequest request, HttpSession session) throws Exception {

        String email = CmmUtil.nvl(request.getParameter("email"));
        String password = CmmUtil.nvl(request.getParameter("password"));

        UserDTO pDTO = UserDTO.builder()
                .email(email)
                .password(password)
                .build();

        UserDTO rDTO = userService.login(pDTO);

        if (rDTO != null) {
            // 세션 저장
            session.setAttribute("LOGIN_USER_ID", rDTO.getUserId());
            session.setAttribute("LOGIN_USER_NAME", rDTO.getName());

            log.info("✅ 로그인 성공 - 세션 저장 완료");
            return rDTO; // 성공 시 DTO 반환
        }

        log.warn("❌ 로그인 실패 - 이메일 또는 비밀번호 불일치");
        return null; // 실패 시 null 반환 (프론트에서 null 체크)
    }

    // ===== 마이페이지 뷰 이동 =====
    @GetMapping("/mypage")
    public String mypage() {
        return "user/mypage";
    }

    // ===== 마이페이지 프로필 조회 =====
    @ResponseBody
    @GetMapping("/mypage/profile")
    public UserDTO getProfile(HttpSession session) throws Exception {
        String userId = CmmUtil.nvl((String) session.getAttribute("LOGIN_USER_ID"));
        if (userId.isEmpty()) {
            log.warn("⚠️ 세션 만료 또는 로그인 안됨");
            return null;
        }

        UserDTO pDTO = UserDTO.builder()
                .userId(userId)
                .build();

        UserDTO dto = userService.getUserProfile(pDTO);
        log.info("📌 프로필 조회 결과: {}", dto);

        return dto;
    }

    // ===== 회원 탈퇴 =====
    @ResponseBody
    @DeleteMapping("/delete")
    public int deleteUser(HttpSession session) throws Exception {
        log.info("🔥 회원탈퇴 컨트롤러 진입");

        String userId = CmmUtil.nvl((String) session.getAttribute("LOGIN_USER_ID"));
        if (userId.isEmpty()) {
            log.warn("⚠️ 로그인 필요 - 세션 없음");
            return 0; // 로그인 안 된 경우 0 반환
        }

        UserDTO pDTO = UserDTO.builder()
                .userId(userId)
                .build();

        int res = userService.deleteUser(pDTO);
        log.info("회원탈퇴 결과 - 삭제된 행 수={}", res);

        session.invalidate();
        return res; // 성공 1 / 실패 0
    }

    // ===== 로그아웃 =====
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        log.info("로그아웃 시작");
        session.invalidate(); // 세션 전체 제거
        log.info("로그아웃 완료 → 메인 페이지로 이동");
        return "redirect:/"; // 홈으로 리다이렉트
    }
    @GetMapping("/login")
    public String login() {
        log.info("GET /user/login");

        return "user/login";  // /WEB-INF/views/user/login.jsp 를 찾음
    }


    // ===== 화면 이동 =====
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
