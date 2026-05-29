package com.lifeops.documentservice.dto;


public record AuthenticatedUserContext(
        String externalId,
        String email,
        String name,
        String provider
) {
}