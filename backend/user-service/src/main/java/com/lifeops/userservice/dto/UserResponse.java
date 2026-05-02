package com.lifeops.userservice.dto;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        String country,
        String provider

) {
}
