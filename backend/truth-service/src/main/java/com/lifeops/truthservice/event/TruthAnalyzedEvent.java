package com.lifeops.truthservice.event;

import java.time.Instant;
import java.util.UUID;

public record TruthAnalyzedEvent(
        UUID eventId,
        UUID analysisId,
        UUID userId,
        String title,
        String claimSummary,
        Integer trustScore,
        String riskLevel,
        String status,
        Instant analyzedAt
) {
}
