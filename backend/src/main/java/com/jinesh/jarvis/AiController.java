package com.jinesh.jarvis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Value("${jarvis.llm.base-url:https://api.openai.com/v1}")
    private String baseUrl;

    @Value("${jarvis.llm.api-key:}")
    private String apiKey;

    @Value("${jarvis.llm.model:gpt-5.6-luna}")
    private String model;

    @PostMapping("/chat")
    public ResponseEntity<Map<String, Object>> chat(
            @RequestBody Map<String, String> body) {

        if (apiKey == null || apiKey.isBlank()) {
            return ResponseEntity.status(500).body(
                    Map.of(
                            "error", "LLM_API_KEY is not configured",
                            "message", "Add LLM_API_KEY in Render Environment."
                    )
            );
        }

        String message = body.getOrDefault("message", "").trim();

        if (message.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "message is required")
            );
        }

        try {
            Map<String, Object> payload = new HashMap<>();

            payload.put("model", model);
            payload.put(
                    "messages",
                    List.of(
                            Map.of(
                                    "role",
                                    "system",
                                    "content",
                                    "You are JARVIS, a personal AI assistant. "
                                    + "Be helpful, concise and honest. "
                                    + "Never claim an action was completed unless it actually happened."
                            ),
                            Map.of(
                                    "role",
                                    "user",
                                    "content",
                                    message
                            )
                    )
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(payload, headers);

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<Map> response =
                    restTemplate.postForEntity(
                            baseUrl + "/chat/completions",
                            request,
                            Map.class
                    );

            Map responseBody = response.getBody();

            if (responseBody == null) {
                return ResponseEntity.status(502).body(
                        Map.of("error", "Empty response from AI provider")
                );
            }

            Object choicesObject = responseBody.get("choices");

            if (!(choicesObject instanceof List<?> choices)
                    || choices.isEmpty()) {

                return ResponseEntity.status(502).body(
                        Map.of(
                                "error",
                                "AI provider returned no choices",
                                "providerResponse",
                                responseBody
                        )
                );
            }

            Object first = choices.get(0);

            if (!(first instanceof Map<?, ?> firstChoice)) {
                return ResponseEntity.status(502).body(
                        Map.of("error", "Invalid AI response")
                );
            }

            Object messageObject = firstChoice.get("message");

            if (!(messageObject instanceof Map<?, ?> aiMessage)) {
                return ResponseEntity.status(502).body(
                        Map.of("error", "AI message missing")
                );
            }

            Object content = aiMessage.get("content");

            return ResponseEntity.ok(
                    Map.of(
                            "reply",
                            content == null ? "" : content.toString(),
                            "model",
                            model
                    )
            );

        } catch (RestClientResponseException e) {

            // Important: expose the provider's HTTP error
            // without exposing the API key.
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(
                            Map.of(
                                    "error",
                                    "AI provider rejected the request",
                                    "providerStatus",
                                    e.getStatusCode().value(),
                                    "providerResponse",
                                    e.getResponseBodyAsString()
                            )
                    );

        } catch (Exception e) {

            return ResponseEntity.status(500).body(
                    Map.of(
                            "error",
                            "JARVIS AI request failed",
                            "message",
                            e.getMessage() == null
                                    ? "Unknown error"
                                    : e.getMessage()
                    )
            );
        }
    }
}
