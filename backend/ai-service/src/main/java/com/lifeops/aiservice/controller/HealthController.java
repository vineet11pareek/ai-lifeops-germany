package com.lifeops.aiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health", description = "AI service health APIs")
public class HealthController {

    @GetMapping("/api/ai/health")
    @Operation(summary = "Health check", description = "Returns basic ai-service health status.")
    public String health(){
        return "AI Service is running";
    }
}
