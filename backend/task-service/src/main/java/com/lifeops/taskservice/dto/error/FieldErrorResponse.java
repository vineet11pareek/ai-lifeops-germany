package com.lifeops.taskservice.dto.error;

public record FieldErrorResponse(
        String field,
        String message
) {
}
