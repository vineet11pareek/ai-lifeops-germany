package com.lifeops.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/health")
@Tag(name = "Health", description = "Basic service health APIs")
public class HealthController {

    @GetMapping
    @Operation(summary = "Health check",
    description = "Return basic user-service health status.")
    public String health(){
        return "User service is running";
    }
}
