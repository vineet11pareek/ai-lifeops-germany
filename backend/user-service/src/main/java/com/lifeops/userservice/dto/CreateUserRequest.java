package com.lifeops.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank(message = "Full name is required")
        @Size(max = 255, message="Full name must not exceed 255 characters")
        String fullName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max=255,message = "Email must not exceed 255 characters")
        String email,

        @Size(max = 100,message = "Country must not exceed 100 characters")
        String country,

        @Size(max = 50, message = "Provider must not exceed 50 character")
        String provider

) {
}
