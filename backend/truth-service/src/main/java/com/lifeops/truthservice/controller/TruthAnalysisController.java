package com.lifeops.truthservice.controller;

import com.lifeops.truthservice.dto.ApiResponse;
import com.lifeops.truthservice.dto.CreateTruthAnalysisRequest;
import com.lifeops.truthservice.dto.TruthAnalysisResponse;
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
            @Valid @RequestBody CreateTruthAnalysisRequest request
    ) {
        return ApiResponse.success(
                "Truth analysis request created successfully",
                truthAnalysisService.createTruthAnalysis(request)
        );
    }

    @GetMapping
    @Operation(
            summary = "Get recent truth analyses",
            description = "Returns recent truth analysis records."
    )
    public ApiResponse<List<TruthAnalysisResponse>> getRecentAnalyses() {
        return ApiResponse.success(
                "Truth analyses fetched successfully",
                truthAnalysisService.getRecentAnalyses()
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get truth analysis by ID",
            description = "Returns truth analysis details for the given ID."
    )
    public ApiResponse<TruthAnalysisResponse> getAnalysisById(
            @PathVariable UUID id
    ) {
        return ApiResponse.success(
                "Truth analysis fetched successfully",
                truthAnalysisService.getAnalysisById(id)
        );
    }
}
