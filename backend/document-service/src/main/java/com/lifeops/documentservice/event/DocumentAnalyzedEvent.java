package com.lifeops.documentservice.event;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record DocumentAnalyzedEvent(
        UUID eventId,
        UUID documentId,
        UUID userId,
        String title,
        String summary,
        String deadlineText,
        String requiredAction,
        String riskLevel,
        String status,
        Instant analyzedAt
) {
}
