package com.lifeops.truthservice.controller;

import com.lifeops.truthservice.common.UserContextHeaders;
import com.lifeops.truthservice.dto.*;
import com.lifeops.truthservice.service.TruthAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/truth")
@Tag(name = "Truth Layer", description = "Truth analysis APIs")
public class TruthAnalysisController {

    private final TruthAnalysisService truthAnalysisService;

    public TruthAnalysisController(TruthAnalysisService truthAnalysisService) {
        this.truthAnalysisService = truthAnalysisService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create truth analysis request",
            description = "Stores content for later truth analysis."
    )
    public ApiResponse<TruthAnalysisResponse> createTruthAnalysis(
            @Valid @RequestBody CreateTruthAnalysisRequest request,
            @RequestHeader(UserContextHeaders.USER_EXTERNAL_ID) String externalId,
            @RequestHeader(UserContextHeaders.USER_EMAIL) String email,
            @RequestHeader(UserContextHeaders.USER_NAME) String name,
            @RequestHeader(UserContextHeaders.AUTH_PROVIDER) String provider
    ) {
        AuthenticatedUserContext userContext = getUserContext(externalId, email, name, provider);
        return ApiResponse.success(
                "Truth analysis request created successfully",
                truthAnalysisService.createTruthAnalysis(request,userContext)
        );
    }

    @GetMapping
    @Operation(
            summary = "Get recent truth analyses",
            description = "Returns recent truth analysis records."
    )
    public ApiResponse<List<TruthAnalysisResponse>> getRecentAnalyses(
            @RequestHeader(UserContextHeaders.USER_EXTERNAL_ID) String externalId,
            @RequestHeader(UserContextHeaders.USER_EMAIL) String email,
            @RequestHeader(UserContextHeaders.USER_NAME) String name,
            @RequestHeader(UserContextHeaders.AUTH_PROVIDER) String provider
    ) {
        AuthenticatedUserContext userContext = getUserContext(externalId, email, name, provider);
        return ApiResponse.success(
                "Truth analyses fetched successfully",
                truthAnalysisService.getRecentAnalyses(userContext)
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get truth analysis by ID",
            description = "Returns truth analysis details for the given ID."
    )
    public ApiResponse<TruthAnalysisResponse> getAnalysisById(
            @PathVariable UUID id,
            @RequestHeader(UserContextHeaders.USER_EXTERNAL_ID) String externalId,
            @RequestHeader(UserContextHeaders.USER_EMAIL) String email,
            @RequestHeader(UserContextHeaders.USER_NAME) String name,
            @RequestHeader(UserContextHeaders.AUTH_PROVIDER) String provider
    ) {
        AuthenticatedUserContext userContext = getUserContext(externalId, email, name, provider);
        return ApiResponse.success(
                "Truth analysis fetched successfully",
                truthAnalysisService.getAnalysisById(id)
        );
    }


    @PostMapping("/analyze")
    @Operation(
            summary = "Analyze content credibility",
            description = "Stores content and analyzes credibility using AI."
    )
    public ApiResponse<TruthAnalysisResponse> analyzeTruth(
            @Valid @RequestBody AnalyzeTruthRequest request,
            @RequestHeader(UserContextHeaders.USER_EXTERNAL_ID) String externalId,
            @RequestHeader(UserContextHeaders.USER_EMAIL) String email,
            @RequestHeader(UserContextHeaders.USER_NAME) String name,
            @RequestHeader(UserContextHeaders.AUTH_PROVIDER) String provider
    ) {
        AuthenticatedUserContext userContext = getUserContext(externalId, email, name, provider);
        return ApiResponse.success(
                "Truth analysis completed successfully",
                truthAnalysisService.analyzeTruth(request,userContext)
        );
    }
    private AuthenticatedUserContext getUserContext(String externalId, String email, String name, String provider){
        return new AuthenticatedUserContext(externalId, email, name, provider);
    }
}
