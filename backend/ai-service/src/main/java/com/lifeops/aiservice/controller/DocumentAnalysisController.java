package com.lifeops.aiservice.controller;

import com.lifeops.aiservice.dto.ApiResponse;
import com.lifeops.aiservice.dto.DocumentAnalysisRequest;
import com.lifeops.aiservice.dto.DocumentAnalysisResponse;
import com.lifeops.aiservice.service.DocumentAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai/document-analysis")
@Tag(
        name = "Document Analysis",
        description = "AI document analysis APIs"
)
public class DocumentAnalysisController {

    private final DocumentAnalysisService documentAnalysisService;

    public DocumentAnalysisController(DocumentAnalysisService documentAnalysisService) {
        this.documentAnalysisService = documentAnalysisService;
    }

    @PostMapping
    @Operation(summary = "Analyze document", description = "Analyzes document text and returns structured summary, deadline, action, risk level, and next step.")
    public ApiResponse<DocumentAnalysisResponse> analyzeDocument(@Valid @RequestBody DocumentAnalysisRequest request){
        return ApiResponse.success(
                "Document analysis completed successfully",
                documentAnalysisService.analyze(request.title(), request.content())
        );
    }
}
