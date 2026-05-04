package com.lifeops.userservice.controller;

import com.lifeops.userservice.dto.ApiResponse;
import com.lifeops.userservice.dto.CreateUserRequest;
import com.lifeops.userservice.dto.UserResponse;
import com.lifeops.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User profile and user management APIs")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user",
            description = "Returns the current demo user profile. Later this will return the authenticated user.")
    public ApiResponse<UserResponse> getCurrentUser(){
       return ApiResponse.success("Current User fetched successfully", userService.getCurrentUser());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create user",
    description = "Creates a new user after validating request data and checking duplicate email.")
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request){
        return ApiResponse.success(
                "User created successfully",userService.createUser(request));

    }


}
