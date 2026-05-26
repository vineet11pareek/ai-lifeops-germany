package com.lifeops.aiservice.controller;

import com.lifeops.aiservice.dto.ApiResponse;
import com.lifeops.aiservice.dto.TruthAnalysisRequest;
import com.lifeops.aiservice.dto.TruthAnalysisResponse;
import com.lifeops.aiservice.service.TruthAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            @Valid @RequestBody TruthAnalysisRequest request
    ) {
        return ApiResponse.success(
                "Truth analysis completed successfully",
                truthAnalysisService.analyze(request.title(), request.content())
        );
    }
}
