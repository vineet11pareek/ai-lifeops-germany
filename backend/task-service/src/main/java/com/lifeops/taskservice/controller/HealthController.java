package com.lifeops.taskservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks/health")
@Tag(
        name = "Health",
        description = "Task service health APIs"
)
public class HealthController {

    @GetMapping
    @Operation(summary = "Health check", description = "Returns basic task-service health status.")
    public String health(){
        return "Task Service is running";
    }
}
