package com.lifeops.aiservice.dto;

public record TruthAnalysisResponse(
        String claimSummary,
        Integer trustScore,
        String riskLevel,
        String explanation,
        String suggestedVerificationSteps
) {
}
