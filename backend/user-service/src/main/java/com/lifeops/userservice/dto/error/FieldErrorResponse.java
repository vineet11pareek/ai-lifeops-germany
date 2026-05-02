package com.lifeops.userservice.dto.error;

public record FieldErrorResponse(
        String field,
        String message
) {
}
