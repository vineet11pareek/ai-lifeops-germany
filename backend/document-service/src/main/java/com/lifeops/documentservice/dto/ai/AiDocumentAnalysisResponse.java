package com.lifeops.documentservice.dto.ai;

public record AiDocumentAnalysisResponse(
        String summary,
        String deadlineText,
        String requiredAction,
        String riskLevel,
        String suggestedNextStep
) {
}
