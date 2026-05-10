package com.lifeops.aiservice.dto;

public record DocumentAnalysisResponse(
        String summary,
        String deadlineText,
        String requiredAction,
        String riskLevel,
        String suggestedNextStep
) {
}
