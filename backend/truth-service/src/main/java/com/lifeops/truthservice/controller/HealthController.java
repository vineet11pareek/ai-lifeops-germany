package com.lifeops.truthservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/truth/health")
@Tag(name = "Health", description = "Truth service health APIs")
public class HealthController {

    @GetMapping
    @Operation(summary = "Health check", description = "Returns basic truth-service health status.")
    public String health(){
        return "Truth service is running";
    }
}
