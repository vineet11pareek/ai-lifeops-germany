package com.lifeops.truthservice.dto.error;

public record FieldErrorResponse(
        String field,
        String message
) {
}
