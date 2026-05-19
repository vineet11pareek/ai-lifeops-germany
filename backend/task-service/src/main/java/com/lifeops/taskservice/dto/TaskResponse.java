package com.lifeops.taskservice.dto;

import java.time.Instant;
import java.util.UUID;

public record TaskResponse(
        UUID id,
        UUID userId,
        String sourceType,
        UUID sourceId,
        String title,
        String description,
        String recommendedAction,
        String riskLevel,
        String status,
        Instant approvedAt,
        Instant rejectedAt,
        Instant createdAt
) {
}
