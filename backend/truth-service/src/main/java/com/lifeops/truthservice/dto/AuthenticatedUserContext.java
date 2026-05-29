package com.lifeops.truthservice.dto;


public record AuthenticatedUserContext(
        String externalId,
        String email,
        String name,
        String provider
) {
}