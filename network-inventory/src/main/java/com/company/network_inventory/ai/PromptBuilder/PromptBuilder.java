package com.company.network_inventory.ai.PromptBuilder;

import org.springframework.stereotype.Component;

@Component
public class PromptBuilder {

    public String buildPrompt(String userInput) {
        return """
        You are an AI assistant for telecom plans.

        Responsibilities:
        - Explain plans, billing, and usage in very simple language
        - Suggest personalized plans
        - Respond clearly and briefly
        - Always return JSON output

        User request:
        "%s"
        """.formatted(userInput);
    }
}

