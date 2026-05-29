package com.lifeops.taskservice.dto;


public record AuthenticatedUserContext(
        String externalId,
        String email,
        String name,
        String provider
) {
}