package kopo.poly.kpaas.controller;

import jakarta.servlet.http.HttpServletRequest;
import kopo.poly.kpaas.dto.MailDTO;
import kopo.poly.kpaas.dto.UserDTO;
import kopo.poly.kpaas.service.IUserService;
import kopo.poly.kpaas.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class EmailController {

    private final IUserService userService;

    // 2. 인증코드 발송
    @PostMapping("/sendResetCode")
    @ResponseBody
    public Map<String, Object> sendResetCode(HttpServletRequest request) {
        log.info("sendResetCode start!");

        Map<String, Object> rMap = new HashMap<>();
        String email = CmmUtil.nvl(request.getParameter("email"));

        try {
            UserDTO user = userService.getUserByEmail(email);

            if (user == null) {
                rMap.put("result", 0);
                rMap.put("msg", "존재하지 않는 이메일입니다.");
            } else {
                String code = String.format("%06d", (int) (Math.random() * 1000000));

                request.getSession().setAttribute("resetCode", code);
                request.getSession().setAttribute("resetCodeExpire", System.currentTimeMillis() + 5 * 60 * 1000);
                request.getSession().setAttribute("resetEmail", email);

                MailDTO pDTO = new MailDTO();
                pDTO.setToMail(email);
                pDTO.setTitle("비밀번호 재설정 인증코드");
                pDTO.setContents("인증코드: " + code + " (5분 이내 유효)");
                userService.doSendMail(pDTO);

                rMap.put("result", 1);
                rMap.put("msg", "인증코드를 이메일로 발송했습니다.");
            }
        } catch (Exception e) {
            log.error("sendResetCode Error : {}", e.getMessage(), e);
            rMap.put("result", -1);
            rMap.put("msg", "인증코드 발송 중 오류 발생");
        }

        log.info("sendResetCode end!");
        return rMap; // ✅ JSON 응답
    }

    // 3. 인증코드 검증
    @PostMapping("/verifyResetCode")
    @ResponseBody
    public Map<String, Object> verifyResetCode(HttpServletRequest request) {
        log.info("verifyResetCode start!");

        Map<String, Object> rMap = new HashMap<>();
        String inputCode = CmmUtil.nvl(request.getParameter("code"));

        try {
            String savedCode = (String) request.getSession().getAttribute("resetCode");
            Long expireAt = (Long) request.getSession().getAttribute("resetCodeExpire");

            if (savedCode == null || expireAt == null || System.currentTimeMillis() > expireAt) {
                rMap.put("result", 0);
                rMap.put("msg", "인증코드가 만료되었습니다.");
            } else if (savedCode.equals(inputCode)) {
                rMap.put("result", 1);
                rMap.put("msg", "인증 성공");
            } else {
                rMap.put("result", 0);
                rMap.put("msg", "인증 실패 - 코드 불일치");
            }
        } catch (Exception e) {
            log.error("verifyResetCode Error : {}", e.getMessage(), e);
            rMap.put("result", -1);
            rMap.put("msg", "인증 처리 중 오류 발생");
        }

        log.info("verifyResetCode end!");
        return rMap; // ✅ JSON 응답
    }

    // 4. 비밀번호 재설정
    @PostMapping("/resetPassword")
    @ResponseBody
    public Map<String, Object> resetPassword(HttpServletRequest request) {
        log.info("resetPassword start!");

        Map<String, Object> rMap = new HashMap<>();
        String newPw = CmmUtil.nvl(request.getParameter("password"));
        String email = (String) request.getSession().getAttribute("resetEmail");

        try {
            if (email == null) {
                rMap.put("result", 0);
                rMap.put("msg", "세션이 만료되었습니다. 다시 시도해주세요.");
            } else {
                UserDTO pDTO = new UserDTO();
                pDTO.setEmail(email);
                pDTO.setPassword(newPw);

                int res = userService.updatePassword(pDTO);

                if (res > 0) {
                    rMap.put("result", 1);
                    rMap.put("msg", "비밀번호가 성공적으로 변경되었습니다.");
                } else {
                    rMap.put("result", 0);
                    rMap.put("msg", "비밀번호 변경 실패");
                }
            }
        } catch (Exception e) {
            log.error("resetPassword Error : {}", e.getMessage(), e);
            rMap.put("result", -1);
            rMap.put("msg", "비밀번호 재설정 중 오류 발생");
        }

        log.info("resetPassword end!");
        return rMap; // ✅ JSON 응답
    }
}
