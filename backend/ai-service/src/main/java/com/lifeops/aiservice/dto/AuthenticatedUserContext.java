package com.lifeops.aiservice.dto;


public record AuthenticatedUserContext(
        String externalId,
        String email,
        String name,
        String provider
) {
}