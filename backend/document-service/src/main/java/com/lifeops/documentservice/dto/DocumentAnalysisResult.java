package com.lifeops.documentservice.dto;

public record DocumentAnalysisResult(
        String summary,
        String deadlineText,
        String requiredAction,
        String riskLevel,
        String suggestedNextStep
) {
}
