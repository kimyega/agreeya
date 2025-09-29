package kopo.poly.kpaas.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GptService {

  @Value("${openai.api.key}")
  private String apiKey;

  private final String CHAT_API_URL = "https://api.openai.com/v1/chat/completions";
  private final String EMBEDDING_API_URL = "https://api.openai.com/v1/embeddings";

  private final ObjectMapper mapper;

  /**
   * GPT-4o 모델을 사용하여 텍스트 생성
   * @param prompt 사용자 입력/프롬프트
   * @return 생성된 텍스트
   */
  public String generateText(String prompt) {
    try {
      RestTemplate restTemplate = new RestTemplate();

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.setBearerAuth(apiKey);

      Map<String, Object> body = new HashMap<>();
      body.put("model", "gpt-4o"); // GPT-4o 사용
      body.put("messages", new Object[]{
              Map.of("role", "user", "content", prompt)
      });
      body.put("temperature", 0.7);       // 온도 조절 가능
      body.put("max_tokens", 2000);        // 토큰 수 제한

      // 요청 로그
      log.info("GPT 요청 Body: {}", new ObjectMapper().writeValueAsString(body));

      HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
      ResponseEntity<String> response = restTemplate.postForEntity(CHAT_API_URL, request, String.class);

      // 응답 로그
      log.info("GPT 응답 Body: {}", response.getBody());

      ObjectMapper mapper = new ObjectMapper();
      JsonNode root = mapper.readTree(response.getBody());
      JsonNode contentNode = root.path("choices").get(0).path("message").path("content");

      if (contentNode.isMissingNode() || contentNode.asText().isBlank()) {
        return "GPT가 텍스트를 생성하지 못했습니다.";
      }

      return contentNode.asText();

    } catch (Exception e) {
      e.printStackTrace();
      return "GPT 호출 실패!";
    }
  }

  // ✅ 임베딩 생성 메서드
  public List<Float> createEmbedding(String text) {
    try {
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.setBearerAuth(apiKey);

      Map<String, Object> body = new HashMap<>();
      body.put("model", "text-embedding-3-small"); // 임베딩 모델
      body.put("input", text);

      HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
      ResponseEntity<String> response = restTemplate.postForEntity(EMBEDDING_API_URL, request, String.class);

      JsonNode root = mapper.readTree(response.getBody());
      JsonNode vectorNode = root.path("data").get(0).path("embedding");

      if (vectorNode.isMissingNode() || !vectorNode.isArray()) {
        log.warn("임베딩 생성 실패: {}", response.getBody());
        return Collections.emptyList();
      }

      List<Float> vector = new ArrayList<>();
      for (JsonNode n : vectorNode) {
        vector.add(n.floatValue());
      }

      return vector;

    } catch (Exception e) {
      log.error("임베딩 생성 실패", e);
      return Collections.emptyList();
    }
  }
}
