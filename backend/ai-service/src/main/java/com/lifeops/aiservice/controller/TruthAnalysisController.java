package com.lifeops.aiservice.controller;

import com.lifeops.aiservice.common.UserContextHeaders;
import com.lifeops.aiservice.dto.ApiResponse;
import com.lifeops.aiservice.dto.AuthenticatedUserContext;
import com.lifeops.aiservice.dto.TruthAnalysisRequest;
import com.lifeops.aiservice.dto.TruthAnalysisResponse;
import com.lifeops.aiservice.service.TruthAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai/truth-analysis")
@Tag(name = "Truth Analysis", description = "AI credibility and claim analysis APIs")
public class TruthAnalysisController {

    private final TruthAnalysisService truthAnalysisService;

    public TruthAnalysisController(TruthAnalysisService truthAnalysisService) {
        this.truthAnalysisService = truthAnalysisService;
    }

    @PostMapping
    @Operation(
            summary = "Analyze content credibility",
            description = "Analyzes pasted content and returns claim summary, trust score, risk level, explanation, and verification steps."
    )
    public ApiResponse<TruthAnalysisResponse> analyzeTruth(
            @Valid @RequestBody TruthAnalysisRequest request,
            @RequestHeader(UserContextHeaders.USER_EXTERNAL_ID) String externalId,
            @RequestHeader(UserContextHeaders.USER_EMAIL) String email,
            @RequestHeader(UserContextHeaders.USER_NAME) String name,
            @RequestHeader(UserContextHeaders.AUTH_PROVIDER) String provider
    ) {
        AuthenticatedUserContext userContext = getUserContext(
                externalId,
                email,
                name,
                provider
        );
        return ApiResponse.success(
                "Truth analysis completed successfully",
                truthAnalysisService.analyze(request.title(), request.content())
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
