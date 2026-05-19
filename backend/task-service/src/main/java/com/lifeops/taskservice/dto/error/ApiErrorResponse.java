package com.lifeops.taskservice.dto.error;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ApiErrorResponse(
        Instant timestamp,
        String correlationId,
        int status,
        String error,
        String message,
        String path,
        List<FieldErrorResponse> fieldErrors
) {
}
