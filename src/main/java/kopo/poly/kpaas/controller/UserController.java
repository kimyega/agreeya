package kopo.poly.kpaas.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.kpaas.dto.UserDTO;
import kopo.poly.kpaas.service.UserService;
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

    private final UserService userService;

    // ===== 로그인 처리 =====
    // ===== 로그인 처리 =====
    @ResponseBody
    @PostMapping("/loginProc")
    public ResponseEntity<?> loginProc(HttpServletRequest request, HttpSession session) {
        try {
            String email = CmmUtil.nvl(request.getParameter("email"));
            String password = CmmUtil.nvl(request.getParameter("password"));

            UserDTO rDTO = userService.login(email, password);

            if (rDTO != null) {
                // 세션 저장
                session.setAttribute("LOGIN_USER_ID", rDTO.getUserId());
                session.setAttribute("LOGIN_USER_NAME", rDTO.getName());

                // ✅ 로그 확인용 출력
                log.info("✅ 로그인 성공 - 세션 저장 완료");
                log.info("   LOGIN_USER_ID   = {}", rDTO.getUserId());
                log.info("   LOGIN_USER_NAME = {}", rDTO.getName());

                return ResponseEntity.ok(Map.of(
                        "res", 1,
                        "msg", "로그인 성공",
                        "user", rDTO
                ));
            } else {
                return ResponseEntity.ok(Map.of(
                        "res", 0,
                        "msg", "이메일 또는 비밀번호가 올바르지 않습니다."
                ));
            }

        } catch (Exception e) {
            log.error("❌ 로그인 에러: ", e);
            return ResponseEntity.status(500).body(Map.of(
                    "res", 2,
                    "msg", "시스템 오류"
            ));
        }
    }


    @GetMapping("/mypage")
    public String mypage() {
        return "user/mypage";  // ✅ 여기서 user/mypage.jsp 를 뷰로 찾아야 함
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
        UserDTO dto = userService.getUserProfile(userId);
        log.info("📌 프로필 조회 결과: {}", dto);
        return dto;
    }

    // ===== 회원 탈퇴 =====
    @ResponseBody
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(HttpSession session) throws Exception {
        log.info("🔥 회원탈퇴 컨트롤러 진입");

        String userId = CmmUtil.nvl((String) session.getAttribute("LOGIN_USER_ID"));
        log.info("회원탈퇴 요청 - 세션 userId={}", userId);

        if (userId.isEmpty()) {
            return ResponseEntity.ok(Map.of("res", 0, "msg", "로그인 필요"));
        }

        int res = userService.deleteUser(userId);
        log.info("회원탈퇴 결과 - 삭제된 행 수={}", res);

        session.invalidate();
        return ResponseEntity.ok(Map.of(
                "res", res,
                "msg", res == 1 ? "탈퇴 성공" : "탈퇴 실패"
        ));
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
