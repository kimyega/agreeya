package kopo.poly.kpaas.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.kpaas.dto.UserDTO;
import kopo.poly.kpaas.service.IUserService;
import kopo.poly.kpaas.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user") // ✅ user 영역 고정
@Controller
public class UserController {

    private final IUserService userService;

    // ===== 로그인 처리 =====
    @ResponseBody
    @PostMapping("/loginProc") //responseEntity바꾸기
    public ResponseEntity<UserDTO> loginProc(HttpServletRequest request, HttpSession session) throws Exception {

        String email = CmmUtil.nvl(request.getParameter("email"));
        String password = CmmUtil.nvl(request.getParameter("password"));

        // ✅ DTO 생성 (암호화/해시는 Service에서 처리)
        UserDTO pDTO = UserDTO.builder()
                .email(email)
                .password(password)
                .build();

        UserDTO rDTO = userService.login(pDTO);

        if (rDTO != null) {
            // 세션 저장 (최소 정보만 저장)
            session.setAttribute("LOGIN_USER_ID", rDTO.getUserId());
            session.setAttribute("LOGIN_USER_NAME", rDTO.getName());

            log.info("✅ 로그인 성공 - 세션 저장 완료");

            // 성공 응답
            UserDTO resDTO = UserDTO.builder()
                    .userId(rDTO.getUserId())
                    .name(rDTO.getName())
                    .build();

            return ResponseEntity.ok(resDTO);

        } else {
            // 실패 응답
            UserDTO resDTO = UserDTO.builder()
                    .build();

            return ResponseEntity.ok(resDTO);
        }
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
    @DeleteMapping("/delete") //responseEntity지우기
    public ResponseEntity<?> deleteUser(HttpSession session) throws Exception {
        log.info("🔥 회원탈퇴 컨트롤러 진입");

        String userId = CmmUtil.nvl((String) session.getAttribute("LOGIN_USER_ID"));
        log.info("회원탈퇴 요청 - 세션 userId={}", userId);

        if (userId.isEmpty()) {
            return ResponseEntity.ok(Map.of("res", 0, "msg", "로그인 필요"));
        }

        UserDTO pDTO = UserDTO.builder()
                .userId(userId)
                .build();

        int res = userService.deleteUser(pDTO);
        log.info("회원탈퇴 결과 - 삭제된 행 수={}", res);

        session.invalidate();
        return ResponseEntity.ok(Map.of(
                "res", res,
                "msg", res == 1 ? "탈퇴 성공" : "탈퇴 실패"
        ));
    }

    // ===== 로그아웃 =====
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        log.info("로그아웃 시작");
        session.invalidate(); // 세션 전체 제거
        log.info("로그아웃 완료 → 메인 페이지로 이동");
        return "/";
    }

    // ===== 화면 이동 =====
    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "user/signup";
    }

    @GetMapping("/findEmail")
    public String findEmail() {
        return "user/findEmail";
    }

    @GetMapping("/findPw")
    public String findPw() {
        return "user/findPw";
    }

    @GetMapping("/phoneVerify")
    public String phoneVerify() {
        return "user/phoneVerify";
    }

    @GetMapping("/emailVerify")
    public String emailVerify() {
        return "user/emailVerify";
    }

    @GetMapping("/changePw")
    public String changePw() {
        return "user/changePw";
    }
}
