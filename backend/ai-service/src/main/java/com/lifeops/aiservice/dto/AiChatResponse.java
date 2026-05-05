package com.lifeops.aiservice.dto;

public record AiChatResponse(
        String answer,
        String provider,
        String model
) {
}
