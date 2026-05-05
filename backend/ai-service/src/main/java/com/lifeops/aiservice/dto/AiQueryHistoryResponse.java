package com.lifeops.aiservice.dto;

import java.time.Instant;
import java.util.UUID;

public record AiQueryHistoryResponse(
        UUID id,
        String question,
        String answer,
        String status,
        String provider,
        String model,
        Instant createdAt
) {
}
