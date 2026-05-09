package com.lifeops.documentservice.dto.ai;

import java.time.Instant;

public record AiApiResponse<T>(
        Instant timestamp,
        boolean success,
        String message,
        T data
) {
}
