package com.lifeops.truthservice.dto.ai;

public record AiTruthAnalysisResponse(
        String claimSummary,
        Integer trustScore,
        String riskLevel,
        String explanation,
        String suggestedVerificationSteps
) {
}
