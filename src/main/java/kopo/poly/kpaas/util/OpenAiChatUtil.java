package kopo.poly.kpaas.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class OpenAiChatUtil {

    @Value("${secure.openai.api-key}")
    private String apiKey;

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build();

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * 노동법 / 근로계약서 Q&A
     */
    public String askLaborLaw(String question) throws IOException {

        Map<String, Object> messageSystem = new HashMap<>();
        messageSystem.put("role", "system");
        messageSystem.put("content",
                "너는 한국, 일본, EU의 노동법과 근로계약서에 정통한 전문 상담가이자 AI 법률 어시스턴트다.\n" +
                        "다음 원칙에 따라 답변하라:\n" +
                        "1. 노동법, 근로계약서와 관련 없는 질문은 정중히 답변을 거절하고, 대신 노동 관련 주제로 다시 유도한다.\n" +
                        "2. 답변 시 가장 먼저 관련 국가의 노동법 조항 번호와 제목을 제시한다.\n" +
                        "3. 실제 근로계약서 작성·해석에서 자주 등장하는 간단한 사례나 예시를 함께 제공한다.\n" +
                        "4. 법률 용어는 일반인이 이해하기 쉽게 풀어서 설명하며, 필요하면 일상적인 비유를 사용한다.\n" +
                        "5. 답변은 핵심 위주로 2~3문단 이내로 간결하게 작성한다.\n" +
                        "6. 사용자가 추가로 질문하기 쉽도록 친절하고 대화체 톤으로 답변한다.");




        Map<String, Object> messageUser = new HashMap<>();
        messageUser.put("role", "user");
        messageUser.put("content", question);

        Map<String, Object> payload = new HashMap<>();
        payload.put("model", "gpt-4o-mini");
        payload.put("messages", new Object[]{messageSystem, messageUser});
        payload.put("max_tokens", 500);
        payload.put("temperature", 0.7);

        String body = mapper.writeValueAsString(payload);
        log.info("Chat 요청 Body={}", body);

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .post(RequestBody.create(body, JSON))
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "null";
                log.error("Chat API Error: {}", errorBody);
                throw new IOException("Unexpected code " + response);
            }

            String responseBody = response.body().string();
            JsonNode root = mapper.readTree(responseBody);
            String answer = root.get("choices").get(0).get("message").get("content").asText();

            return answer.trim();
        }
    }
}
