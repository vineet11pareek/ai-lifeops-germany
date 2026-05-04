package com.lifeops.userservice.dto;

public record AuthenticatedUser(
        String externalId,
        String fullName,
        String email,
        String provider
) {
}
