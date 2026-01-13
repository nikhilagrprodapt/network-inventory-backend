package com.company.network_inventory.ai.Controller;

import com.company.network_inventory.ai.Dto.ChatRequest;
import com.company.network_inventory.ai.Dto.ChatResponse;
import com.company.network_inventory.ai.Service.AiChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/ai")
public class AiChatController {

    private final AiChatService aiChatService;

    public AiChatController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {
        String reply = aiChatService.chat(request.getMessage());
        ChatResponse response = new ChatResponse();
        response.setReply(reply);
        return response;
    }
}

