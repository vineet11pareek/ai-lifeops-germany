package com.lifeops.documentservice.dto.error;

public record FieldErrorResponse(
        String field,
        String message
) {
}
