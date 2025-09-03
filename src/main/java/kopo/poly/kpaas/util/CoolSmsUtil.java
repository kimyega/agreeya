//package kopo.poly.kpaas.util;
//
//import lombok.extern.slf4j.Slf4j;
//import net.nurigo.sdk.NurigoApp;
//import net.nurigo.sdk.message.model.Message;
//import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
//import net.nurigo.sdk.message.response.SingleMessageSentResponse;
//import net.nurigo.sdk.message.service.DefaultMessageService;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import jakarta.annotation.PostConstruct;
//
//@Slf4j
//@Component
//public class CoolSmsUtil {
//
//    private final String apiKey;
//    private final String apiSecret;
//    private final String sender;
//
//    private DefaultMessageService messageService;
//
//    public CoolSmsUtil(@Value("${coolsms.api.key}") String apiKey,
//                       @Value("${coolsms.api.secret}") String apiSecret,
//                       @Value("${coolsms.sender}") String sender) {
//        this.apiKey = apiKey;
//        this.apiSecret = apiSecret;
//        this.sender = sender;
//    }
//
//    @PostConstruct
//    private void init() {
//        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
//    }
//
//    /**
//     * SMS 인증번호 발송
//     *
//     * @param to   수신자 전화번호 (010xxxxxxxx)
//     * @param code 인증번호 (6자리)
//     */
//    public void sendVerificationCode(String to, String code) {
//        Message message = new Message();
//        message.setFrom(sender);  // 발신자 번호 (등록된 번호만 가능)
//        message.setTo(to);        // 수신자 번호
//        message.setText("[안심계약] 인증번호: " + code + "\n(본 문자는 발신 전용입니다)");
//
//        try {
//            SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
//            log.info("✅ CoolSMS 전송 성공: {}", response);
//        } catch (Exception e) {
//            log.error("❌ CoolSMS 전송 실패: {}", e.getMessage());
//            throw new RuntimeException("SMS 전송 실패: " + e.getMessage(), e);
//        }
//    }
//}
