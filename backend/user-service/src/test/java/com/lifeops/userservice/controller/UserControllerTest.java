package com.lifeops.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lifeops.userservice.dto.AuthenticatedUser;
import com.lifeops.userservice.dto.CreateUserRequest;
import com.lifeops.userservice.dto.UserResponse;
import com.lifeops.userservice.service.GoogleTokenVerifierService;
import com.lifeops.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private GoogleTokenVerifierService googleTokenVerifierService;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());


    @Test
    void shouldCreateUserSuccessfully() throws Exception{
        UserResponse response = new UserResponse(
                UUID.randomUUID(),
                "Test User",
                "test.user@example.com",
                "Germany",
                "Google"
        );

        Mockito.when(userService.createUser(any(CreateUserRequest.class))).thenReturn(response);

        CreateUserRequest request = new CreateUserRequest(
                "Test User",
                "test.user@example.com",
                "Germany",
                "GOOGLE"
        );

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User created successfully"))
                .andExpect(jsonPath("$.data.name").value("Test User"))
                .andExpect(jsonPath("$.data.email").value("test.user@example.com"));
    }

    @Test
    void shouldReturnBadRequestForInvalidRequest() throws Exception{

        CreateUserRequest request = new CreateUserRequest(
                "",
                "test.user@example.com",
                "Germany",
                "Google"
        );

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.fieldErrors").isArray());
    }

    @Test
    void shouldReturnAuthenticatedUser() throws Exception{
        UserResponse response = new UserResponse(
                UUID.randomUUID(),
                "Test User",
                "test.user@example.com",
                "Germany",
                "GOOGLE"
        );


        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                "google-sub-123",
                "Test User",
                "test.user@example.com",
                "GOOGLE"
        );

        Mockito.when(googleTokenVerifierService.verify("test-token"))
                .thenReturn(authenticatedUser);
        Mockito.when(userService.getOrCreateAuthenticatedUser(any(AuthenticatedUser.class)))
                .thenReturn(response);

        mockMvc.perform(get("/api/users/me")
                        .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Current user fetched successfully"))
                .andExpect(jsonPath("$.data.name").value("Test User"))
                .andExpect(jsonPath("$.data.email").value("test.user@example.com"));

        Mockito.verify(googleTokenVerifierService).verify("test-token");
        Mockito.verify(userService).getOrCreateAuthenticatedUser(any(AuthenticatedUser.class));
    }
}
