package com.lifeops.taskservice.event;

import java.time.Instant;
import java.util.UUID;

public record DocumentEventAnalyzed(
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
