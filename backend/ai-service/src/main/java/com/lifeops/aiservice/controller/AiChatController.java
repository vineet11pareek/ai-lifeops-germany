package com.lifeops.aiservice.controller;

import com.lifeops.aiservice.common.UserContextHeaders;
import com.lifeops.aiservice.dto.*;
import com.lifeops.aiservice.service.AiChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ApiResponse<AiChatResponse> ask(@Valid @RequestBody AiChatRequest request,
                                           @RequestHeader(UserContextHeaders.USER_EXTERNAL_ID) String externalId,
                                           @RequestHeader(UserContextHeaders.USER_EMAIL) String email,
                                           @RequestHeader(UserContextHeaders.USER_NAME) String name,
                                           @RequestHeader(UserContextHeaders.AUTH_PROVIDER) String provider){
        AuthenticatedUserContext userContext = getUserContext(
                externalId,
                email,
                name,
                provider
        );
        return ApiResponse.success(
                "AI response generated successfully",
                aiChatService.ask(request.question(), userContext)
        );
    }

    @GetMapping("/queries")
    @Operation(summary = "Get recent AI queries",
    description = "Returns recent AI query history.")
    public ApiResponse<List<AiQueryHistoryResponse>> getRecentQueries(
            @RequestHeader(UserContextHeaders.USER_EXTERNAL_ID) String externalId,
            @RequestHeader(UserContextHeaders.USER_EMAIL) String email,
            @RequestHeader(UserContextHeaders.USER_NAME) String name,
            @RequestHeader(UserContextHeaders.AUTH_PROVIDER) String provider
    ){
        AuthenticatedUserContext userContext = getUserContext(
                externalId,
                email,
                name,
                provider
        );
        return ApiResponse.success(
                "AI query history fetched successfully",
                aiChatService.getRecentQueries(userContext)
        );
    }

    private AuthenticatedUserContext getUserContext(
            String externalId,
            String email,
            String name,
            String provider
    ) {
        return new AuthenticatedUserContext(
                externalId,
                email,
                name,
                provider
        );
    }
}
