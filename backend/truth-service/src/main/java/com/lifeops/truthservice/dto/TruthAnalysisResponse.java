package com.lifeops.truthservice.dto;

import java.time.Instant;
import java.util.UUID;

public record TruthAnalysisResponse(
        UUID id,
        String title,
        String content,
        String claimSummary,
        Integer trustScore,
        String riskLevel,
        String explanation,
        String suggestedVerificationSteps,
        String status,
        Instant createdAt
) {
}
