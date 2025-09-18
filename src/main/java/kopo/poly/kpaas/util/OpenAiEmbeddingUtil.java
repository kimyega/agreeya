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
public class OpenAiEmbeddingUtil {

    @Value("${secure.openai.api-key}")
    private String apiKey;

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    // ✅ Timeout 설정 강화된 OkHttpClient
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)   // 서버 연결 대기
            .writeTimeout(60, TimeUnit.SECONDS)     // 요청 본문 전송
            .readTimeout(120, TimeUnit.SECONDS)     // 응답 읽기
            .build();

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * 긴 텍스트도 안전하게 Embedding → 평균 벡터 반환
     */
    //XML-> 조문별 DTO여러개 -> 긴 텍스트는 Util에서 쪼개서 백터롸 -> DB에 저장
    public List<Float> createEmbedding(String inputText) throws Exception {

        inputText = CmmUtil.nvl(inputText);

        // ✅ 2000자 단위 분할
        List<String> chunks = splitText(inputText, 2000);

        List<List<Float>> allVectors = new ArrayList<>();

        for (String chunk : chunks) {
            List<Float> vec = createEmbeddingSingle(chunk);
            allVectors.add(vec);
        }

        return averageVectors(allVectors);
    }

    /**
     * 실제 API 한 번 호출 (단일 chunk 처리)
     */
    private List<Float> createEmbeddingSingle(String inputText) throws IOException {

        Map<String, Object> payload = new HashMap<>();
        payload.put("input", inputText);
        payload.put("model", "text-embedding-3-small"); //모델버전

        String body = mapper.writeValueAsString(payload);
        log.info("Embedding 요청 Body={}", body);

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/embeddings")
                .post(RequestBody.create(body, JSON))
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "null";
                log.error("Embedding API Error: {}", errorBody);
                throw new IOException("Unexpected code " + response);
            }

            String responseBody = response.body().string();
            JsonNode root = mapper.readTree(responseBody);
            JsonNode embeddingNode = root.get("data").get(0).get("embedding");

            List<Float> vector = new ArrayList<>();
            embeddingNode.forEach(v -> vector.add(v.floatValue()));

            return vector;
        }
    }

    /** 텍스트 분할 */
    private List<String> splitText(String text, int maxLen) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < text.length(); i += maxLen) {
            int end = Math.min(i + maxLen, text.length());
            result.add(text.substring(i, end));
        }
        return result;
    }

    /** 여러 벡터 평균 */
    private List<Float> averageVectors(List<List<Float>> vectors) {
        int size = vectors.get(0).size();
        List<Float> avg = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            float sum = 0;
            for (List<Float> vec : vectors) {
                sum += vec.get(i);
            }
            avg.add(sum / vectors.size());
        }
        return avg;
    }
}
