package com.lifeops.documentservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/documents")
@Tag(name = "Health", description = "Document service health APIs")
public class HealthController {

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Returns basic document-service health status.")
    public String health(){
        return "Document Service is running";
    }
}
