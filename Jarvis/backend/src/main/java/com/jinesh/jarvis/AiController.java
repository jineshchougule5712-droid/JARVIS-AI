package com.jinesh.jarvis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {
  @Value("${jarvis.llm.base-url}") String baseUrl;
  @Value("${jarvis.llm.api-key:}") String apiKey;
  @Value("${jarvis.llm.model:gpt-4o-mini}") String model;

  @PostMapping("/chat")
  public Map<String,Object> chat(@RequestBody Map<String,String> body) {
    if (apiKey == null || apiKey.isBlank())
      return Map.of("reply","LLM_API_KEY is not configured on the JARVIS server.");

    Map<String,Object> payload = new HashMap<>();
    payload.put("model", model);
    payload.put("messages", List.of(
      Map.of("role","system","content","You are JARVIS, a concise personal AI assistant. Never claim an action was performed unless it actually was. Ask for confirmation before consequential actions."),
      Map.of("role","user","content",body.getOrDefault("message",""))
    ));

    HttpHeaders h = new HttpHeaders();
    h.setContentType(MediaType.APPLICATION_JSON);
    h.setBearerAuth(apiKey);
    ResponseEntity<Map> r = new RestTemplate().postForEntity(
      baseUrl + "/chat/completions", new HttpEntity<>(payload,h), Map.class);

    Object choices = r.getBody() == null ? null : r.getBody().get("choices");
    if (choices instanceof List<?> list && !list.isEmpty()) {
      Object first = list.get(0);
      if (first instanceof Map<?,?> fm) {
        Object msg = fm.get("message");
        if (msg instanceof Map<?,?> mm)
          return Map.of("reply", String.valueOf(mm.getOrDefault("content","")));
      }
    }
    return Map.of("reply","The AI provider returned an unexpected response.");
  }
}
