package com.lifeops.userservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/health")
public class HealthController {
    @GetMapping
    public String health(){
        return "User service is running";
    }
}
