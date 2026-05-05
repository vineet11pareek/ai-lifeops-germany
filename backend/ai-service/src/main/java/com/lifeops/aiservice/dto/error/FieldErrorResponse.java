package com.lifeops.aiservice.dto.error;

public record FieldErrorResponse(
        String field,
        String message
) {
}
