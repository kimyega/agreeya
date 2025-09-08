package kopo.poly.kpaas.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kopo.poly.kpaas.dto.MailDTO;
import kopo.poly.kpaas.dto.ResultDTO;
import kopo.poly.kpaas.dto.UserDTO;
import kopo.poly.kpaas.service.IUserService;
import kopo.poly.kpaas.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * User 데이터 관리 컨트롤러
 *  - 이메일 관련 로직(사용자정보 조회, 인증코드 검증)
 *  - 비밀번호 관련 로직(초기화, 재설정 코드 검증)
 *  ...
 */
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class EmailController {

    private final IUserService userService;


    @PostMapping("/userFindEmail")
    @ResponseBody
    public ResultDTO findEmail(UserDTO pDTO, HttpServletRequest request) {
        log.info("findEmail start!");

        String name = CmmUtil.nvl(pDTO.getName());
        String tel = CmmUtil.nvl(pDTO.getTel());

        try {
            UserDTO rDto = userService.getUserByNameAndPhone(name, tel);

            if (rDto == null) {
                return ResultDTO.builder()
                        .result(0)
                        .msg("등록되지 않은 사용자입니다.")
                        .build();
            }

            String code = userService.sendFindEmailCode(name, tel);

            HttpSession session = request.getSession();
            session.setAttribute("findEmailCode", code);
            session.setAttribute("findEmailTel", tel);
            session.setAttribute("findEmailName", name);
            session.setAttribute("findEmailExpire", System.currentTimeMillis() + 5 * 60 * 1000);

            return ResultDTO.builder()
                    .result(1)
                    .msg("인증번호를 발송했습니다.")
                    .build();

        } catch (Exception e) {
            log.error("findEmail Error: {}", e.getMessage(), e);
            return ResultDTO.builder().result(-1).msg("이메일 찾기 중 오류 발생").build();
        } finally {
            log.info("findEmail end!");
        }
    }


    @PostMapping("/userVerifyFindEmail")
    @ResponseBody
    public ResultDTO verifyFindEmail(UserDTO pDTO, HttpServletRequest request) {
        log.info("verifyFindEmail start!");

        String inputCode = CmmUtil.nvl(request.getParameter("code"));

        try {
            HttpSession session = request.getSession();
            String savedCode = (String) session.getAttribute("findEmailCode");
            String name = (String) session.getAttribute("findEmailName");
            String tel = (String) session.getAttribute("findEmailTel");
            Long expireAt = (Long) session.getAttribute("findEmailExpire");

            if (savedCode == null || expireAt == null || System.currentTimeMillis() > expireAt) {
                return ResultDTO.builder()
                        .result(0)
                        .msg("인증번호가 만료되었습니다.")
                        .build();
            }

            if (!savedCode.equals(inputCode)) {
                return ResultDTO.builder()
                        .result(0)
                        .msg("인증번호가 일치하지 않습니다.")
                        .build();
            }

            UserDTO rDto = userService.getUserByNameAndPhone(name, tel);
            if (rDto == null) {
                return ResultDTO.builder()
                        .result(0)
                        .msg("사용자를 찾을 수 없습니다.")
                        .build();
            }

            String maskedEmail = userService.maskEmail(rDto.getEmail());
            return ResultDTO.builder()
                    .result(1)
                    .msg("가입된 이메일: " + maskedEmail)
                    .data(maskedEmail)
                    .build();

        } catch (Exception e) {
            log.error("verifyFindEmail Error: {}", e.getMessage(), e);
            return ResultDTO.builder().result(-1).msg("인증 처리 중 오류 발생").build();
        } finally {
            log.info("verifyFindEmail end!");
        }
    }


    @PostMapping("/sendResetCode")
    @ResponseBody
    public ResultDTO sendResetCode(UserDTO pDTO, HttpServletRequest request) {
        log.info("sendResetCode start!");

        String email = CmmUtil.nvl(pDTO.getEmail());

        try {
            UserDTO user = userService.getUserByEmail(email);

            if (user == null) {
                return ResultDTO.builder().result(0).msg("존재하지 않는 이메일입니다.").build();
            }

            String code = String.format("%06d", (int) (Math.random() * 1000000));
            HttpSession session = request.getSession();
            session.setAttribute("resetCode", code);
            session.setAttribute("resetCodeExpire", System.currentTimeMillis() + 5 * 60 * 1000);
            session.setAttribute("resetEmail", email);

            userService.doSendMail(MailDTO.builder()
                    .toMail(email)
                    .title("비밀번호 재설정 인증코드")
                    .contents("인증코드: " + code + " (5분 이내 유효)")
                    .build());

            return ResultDTO.builder()
                    .result(1)
                    .msg("인증코드를 이메일로 발송했습니다.")
                    .build();

        } catch (Exception e) {
            log.error("sendResetCode Error : {}", e.getMessage(), e);
            return ResultDTO.builder()
                    .result(-1)
                    .msg("인증코드 발송 중 오류 발생")
                    .build();
        } finally {
            log.info("sendResetCode end!");
        }
    }


    @PostMapping("/verifyResetCode")
    @ResponseBody
    public ResultDTO verifyResetCode(UserDTO pDTO, HttpServletRequest request) {
        log.info("verifyResetCode start!");

        String inputCode = CmmUtil.nvl(request.getParameter("code"));

        try {
            HttpSession session = request.getSession();
            String savedCode = (String) session.getAttribute("resetCode");
            Long expireAt = (Long) session.getAttribute("resetCodeExpire");
            // 인증코드 검증 로직
            if (savedCode == null || expireAt == null || System.currentTimeMillis() > expireAt) {
                return ResultDTO.builder()
                        .result(0)
                        .msg("인증코드가 만료되었습니다.")
                        .build();
            }

            if (savedCode.equals(inputCode)) {
                return ResultDTO.builder()
                        .result(1)
                        .msg("인증 성공")
                        .build();
            } else {
                return ResultDTO.builder()
                        .result(0)
                        .msg("인증 실패 - 코드 불일치")
                        .build();
            }

        } catch (Exception e) {
            log.error("verifyResetCode Error : {}", e.getMessage(), e);
            return ResultDTO.builder().result(-1).msg("인증 처리 중 오류 발생").build();
        } finally {
            log.info("verifyResetCode end!");
        }
    }


    @PostMapping("/resetPassword")
    @ResponseBody
    public ResultDTO resetPassword(UserDTO pDTO, HttpServletRequest request) {
        log.info("resetPassword start!");

        String newPw = CmmUtil.nvl(pDTO.getPassword());
        String email = (String) request.getSession().getAttribute("resetEmail");

        try {
            if (email == null) {
                return ResultDTO.builder()
                        .result(0)
                        .msg("세션이 만료되었습니다. 다시 시도해주세요.")
                        .build();
            }

            int res = userService.updatePassword(UserDTO.builder()
                    .email(email)
                    .password(newPw)
                    .build());

            if (res > 0) {
                return ResultDTO.builder()
                        .result(1)
                        .msg("비밀번호가 성공적으로 변경되었습니다.")
                        .build();
            } else {
                return ResultDTO.builder()
                        .result(0)
                        .msg("비밀번호 변경 실패")
                        .build();
            }

        } catch (Exception e) {
            log.error("resetPassword Error : {}", e.getMessage(), e);
            return ResultDTO.builder()
                    .result(-1)
                    .msg("비밀번호 재설정 중 오류 발생")
                    .build();
        } finally {
            log.info("resetPassword end!");
        }
    }

    // 1. 이름 + 전화번호 확인 → 인증번호 발송
    @PostMapping("/userFindEmail")
    @ResponseBody
    public Map<String, Object> findEmail(HttpServletRequest request) {
        log.info("findEmail start!");

        String name = CmmUtil.nvl(request.getParameter("name"));
        String tel = CmmUtil.nvl(request.getParameter("tel"));

        Map<String, Object> rMap = new HashMap<>();

        try {
            UserDTO rDto = userService.getUserByNameAndPhone(name, tel);

            if (rDto == null) {
                rMap.put("result", 0);
                rMap.put("msg", "등록되지 않은 사용자입니다.");
            } else {
                userService.sendFindEmailCode(request, name, tel);
                rMap.put("result", 1);
                rMap.put("msg", "인증번호를 발송했습니다.");
            }
        } catch (Exception e) {
            rMap.put("result", -1);
            rMap.put("msg", "이메일 찾기 중 오류 발생");
            log.error("findEmail Error: {}", e.getMessage(), e);
        }

        log.info("findEmail end!");
        return rMap;
    }

    // 2. 인증번호 검증 → 이메일 반환
    @PostMapping("/userVerifyFindEmail")
    @ResponseBody
    public Map<String, Object> verifyFindEmail(HttpServletRequest request) {
        log.info("verifyFindEmail start!");

        String inputCode = CmmUtil.nvl(request.getParameter("code"));
        Map<String, Object> rMap = new HashMap<>();

        try {
            HttpSession session = request.getSession();
            String savedCode = (String) session.getAttribute("findEmailCode");
            String name = (String) session.getAttribute("findEmailName");
            String tel = (String) session.getAttribute("findEmailTel");
            Long expireAt = (Long) session.getAttribute("findEmailExpire");

            if (savedCode == null || expireAt == null || System.currentTimeMillis() > expireAt) {
                rMap.put("result", 0);
                rMap.put("msg", "인증번호가 만료되었습니다.");
            } else if (savedCode.equals(inputCode)) {
                UserDTO rDto = userService.getUserByNameAndPhone(name, tel);

                if (rDto != null) {
                    String maskedEmail = userService.maskEmail(rDto.getEmail());
                    rMap.put("result", 1);
                    rMap.put("msg", "가입된 이메일: " + maskedEmail);
                } else {
                    rMap.put("result", 0);
                    rMap.put("msg", "사용자를 찾을 수 없습니다.");
                }
            } else {
                rMap.put("result", 0);
                rMap.put("msg", "인증번호가 일치하지 않습니다.");
            }
        } catch (Exception e) {
            rMap.put("result", -1);
            rMap.put("msg", "인증 처리 중 오류 발생");
            log.error("verifyFindEmail Error: {}", e.getMessage(), e);
        }

        log.info("verifyFindEmail end!");
        return rMap;
    }
}

