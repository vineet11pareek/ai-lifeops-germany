package com.lifeops.documentservice.dto;

import java.time.Instant;
import java.util.UUID;

public record DocumentResponse(
        UUID id,
        String title,
        String content,
        String summary,
        String deadlineText,
        String requiredAction,
        String riskLevel,
        String suggestedNextStep,
        String status,
        Instant createdAt
) {
}
