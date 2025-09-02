package kopo.poly.kpaas.controller;

import kopo.poly.kpaas.dto.EmailCheckRequestDTO;
import kopo.poly.kpaas.service.UserService;
import kopo.poly.kpaas.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user") // 💡 prefix 추가
public class UserApiController {

    private final UserService userService;

    @PostMapping("/emailCheck")
    public ResponseEntity<String> emailCheck(@RequestBody EmailCheckRequestDTO pDTO) {
        log.info("💌 이메일 인증 요청: {}", pDTO.getUserEmail());


        try {
            String email = userService.checkEmailAndSendCode(pDTO.getUserEmail());
            return ResponseEntity.ok(email);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("이메일이 존재하지 않습니다.");
        }
    }

    // 클라이언트로부터 POST 요청을 받는 메서드로, URL은 /email/verify
    @PostMapping("/email/verify")
// 응답을 HTTP 본문(ResponseBody)에 바로 담아 반환 (뷰가 아닌 JSON/text로 응답)
    @ResponseBody
// 이메일과 인증 코드를 요청 파라미터로 받아서 인증을 처리하는 메서드
    public ResponseEntity<String> verifyEmailCode(@RequestParam String email,
                                                  @RequestParam String code) {
        // UserService에서 이메일과 코드가 일치하는지 검증하는 로직 호출
        // 인증이 성공한 경우

        try {
            boolean result = userService.verifyCode(email, code);
            return ResponseEntity.ok("인증 완료");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 비밀번호 변경 처리
    @PostMapping("/changePw")
    @ResponseBody
    public ResponseEntity<String> changePassword(@RequestParam String email,
                                                 @RequestParam String newPassword) {
        try {
            // 실제 비밀번호 업데이트 로직 호출
            userService.updatePassword(email, newPassword);
            return ResponseEntity.ok("비밀번호 변경 성공");
        } catch (Exception e) {
            log.error("비밀번호 변경 실패", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ===== 이메일 찾기: 코드 요청 =====
    @PostMapping("/find-email/request")
    public ResponseEntity<?> requestFindEmailCode(@RequestBody Map<String, String> req) {
        try {
            String phone = CmmUtil.nvl(req.get("phone"));
            userService.sendFindEmailCode(phone);
            return ResponseEntity.ok(Map.of("ok", true, "message", "인증번호를 전송했습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("ok", false, "message", e.getMessage()));
        } catch (Exception e) {
            log.error("find-email/request error", e);
            return ResponseEntity.internalServerError().body(Map.of("ok", false, "message", "처리 중 오류가 발생했습니다."));
        }
    }

    // ===== 이메일 찾기: 코드 검증 =====
    @PostMapping("/find-email/verify")
    public ResponseEntity<?> verifyFindEmailCode(@RequestBody Map<String, String> req) {
        try {
            String phone = CmmUtil.nvl(req.get("phone"));
            String code  = CmmUtil.nvl(req.get("code"));
            String maskedEmail = userService.verifyFindEmailCode(phone, code);
            return ResponseEntity.ok(Map.of("ok", true, "email", maskedEmail));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("ok", false, "message", e.getMessage()));
        } catch (Exception e) {
            log.error("find-email/verify error", e);
            return ResponseEntity.internalServerError().body(Map.of("ok", false, "message", "처리 중 오류가 발생했습니다."));
        }
    }

}
