package kopo.poly.kpaas.controller;

import jakarta.servlet.http.HttpSession;
import kopo.poly.kpaas.service.MypageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mypage")
public class MypageApi {

    private final MypageService mypageService;

    public MypageApi(MypageService mypageService) {
        this.mypageService = mypageService;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(HttpSession s) {
        String userId = (String) s.getAttribute("LOGIN_USER_ID");
        if (userId == null) return ResponseEntity.status(401).body("UNAUTHORIZED");
        return ResponseEntity.ok(mypageService.getProfile(userId));
    }
}
