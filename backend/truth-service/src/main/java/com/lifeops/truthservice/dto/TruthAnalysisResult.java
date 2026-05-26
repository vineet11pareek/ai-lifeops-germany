package com.lifeops.truthservice.dto;

public record TruthAnalysisResult(
        String claimSummary,
        Integer trustScore,
        String riskLevel,
        String explanation,
        String suggestedVerificationSteps
) {
}
