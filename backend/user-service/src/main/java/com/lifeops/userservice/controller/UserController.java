package com.lifeops.userservice.controller;

import com.lifeops.userservice.dto.ApiResponse;
import com.lifeops.userservice.dto.AuthenticatedUser;
import com.lifeops.userservice.dto.CreateUserRequest;
import com.lifeops.userservice.dto.UserResponse;
import com.lifeops.userservice.exception.InvalidAuthTokenException;
import com.lifeops.userservice.service.GoogleTokenVerifierService;
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

    private final GoogleTokenVerifierService googleTokenVerifierService;

    public UserController(UserService userService, GoogleTokenVerifierService googleTokenVerifierService) {
        this.userService = userService;
        this.googleTokenVerifierService = googleTokenVerifierService;
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user",
            description = "Returns the current demo user profile. Later this will return the authenticated user.")
    public ApiResponse<UserResponse> getCurrentUser(@RequestHeader("Authorization") String authorizationHeader){
       String token  = extractBearerToken(authorizationHeader);
        AuthenticatedUser authenticatedUser = googleTokenVerifierService.verify(token);
        return ApiResponse.success(
                "Current user fetched successfully",
                userService.getOrCreateAuthenticatedUser(authenticatedUser)
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create user",
    description = "Creates a new user after validating request data and checking duplicate email.")
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request){
        return ApiResponse.success(
                "User created successfully",userService.createUser(request));

    }

    private String extractBearerToken(String authorizationHeader){
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            throw new InvalidAuthTokenException("Missing or invalid Authorization header");
        }
        return authorizationHeader.substring(7);
    }


}
