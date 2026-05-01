package com.lifeops.userservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/users/me")
public class UserController {

    @GetMapping
    public Map<String, Object> getCurrentUser(){
        return Map.of(
                "id", "user-1",
                "name", "Vineet",
                "email", "vineet@example.com",
                "country", "Germany"
        );
    }
}
