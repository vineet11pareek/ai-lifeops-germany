package com.lifeops.aiservice.controller;

import com.lifeops.aiservice.dto.AiChatRequest;
import com.lifeops.aiservice.dto.AiChatResponse;
import com.lifeops.aiservice.dto.ApiResponse;
import com.lifeops.aiservice.service.AiChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@Tag(name = "Ai Chat", description = "Basic AI query APIs")
public class AiChatController {

    private final AiChatService aiChatService;

    public AiChatController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @PostMapping("/chat")
    @Operation(summary = "Ask AI question",
    description = "Processes a user question using Spring AI and returns an AI-generated answer.")
    public ApiResponse<AiChatResponse> ask(@Valid @RequestBody AiChatRequest request){
        return ApiResponse.success(
                "AI response generated successfully",
                aiChatService.ask(request.question())
        );
    }
}
