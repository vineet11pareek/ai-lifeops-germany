package com.lifeops.aiservice.event;

import java.time.Instant;
import java.util.UUID;

public record AiQueryCompletedEvent(
        UUID eventId,
        UUID queryId,
        UUID userId,
        String question,
        String status,
        String provider,
        String model,
        Instant completedAt
) {
}
