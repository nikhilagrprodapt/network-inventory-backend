package com.company.network_inventory.ai.Service;

import com.company.network_inventory.ai.PromptBuilder.PromptBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;




@Service
public class AiChatService {
    private String url="https://api.groq.com/openai/v1/chat/completions";

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.model}")
    private String model;

    @Value("${groq.api.temperature}")
    private double temperature;

    private final RestTemplate restTemplate;
    private final PromptBuilder promptBuilder;

    public AiChatService(RestTemplate restTemplate, PromptBuilder promptBuilder) {
        this.restTemplate = restTemplate;
        this.promptBuilder = promptBuilder;
    }

    public String chat(String userMessage) {

        String prompt = promptBuilder.buildPrompt(userMessage);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("temperature", temperature);
        body.put("messages", List.of(message));

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        Map<?, ?> response = restTemplate.postForObject(
                "https://api.groq.com/openai/v1/chat/completions",
                request,
                Map.class
        );

        return extractText(response);
    }

    private String extractText(Map<?, ?> response) {
        try {
            List<?> choices = (List<?>) response.get("choices");
            Map<?, ?> first = (Map<?, ?>) choices.get(0);
            Map<?, ?> message = (Map<?, ?>) first.get("message");
            return message.get("content").toString();
        }  catch (Exception e) {
        return "AI error: failed to parse response.";
    }
}
}
