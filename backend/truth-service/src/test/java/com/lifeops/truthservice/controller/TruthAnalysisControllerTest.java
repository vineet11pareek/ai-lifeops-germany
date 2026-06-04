package com.lifeops.truthservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifeops.truthservice.common.UserContextHeaders;
import com.lifeops.truthservice.dto.AnalyzeTruthRequest;
import com.lifeops.truthservice.dto.AuthenticatedUserContext;
import com.lifeops.truthservice.dto.CreateTruthAnalysisRequest;
import com.lifeops.truthservice.dto.TruthAnalysisResponse;
import com.lifeops.truthservice.service.TruthAnalysisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TruthAnalysisController.class)
class TruthAnalysisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TruthAnalysisService service;

    private ObjectMapper objectMapper = new ObjectMapper();

    private AuthenticatedUserContext userContext;

    @BeforeEach
    void setup(){
        userContext = new AuthenticatedUserContext(
                "google-sub-123",
                "User@test.com",
                "Test User",
                "GOOGLE"
        );
    }

    @Test
    void shouldCreateTruthAnalysisRequest() throws Exception{

        //Given
        TruthAnalysisResponse response = sampleResponse("RECEIVED");

        //when
        when(service.createTruthAnalysis(any(CreateTruthAnalysisRequest.class),any(AuthenticatedUserContext.class))).thenReturn(response);

        CreateTruthAnalysisRequest request = new CreateTruthAnalysisRequest(
                "Online claim about Bürgergeld",
                "Everyone can get Bürgergeld without checks."
        );

        mockMvc.perform(post("/api/truth")
                .contentType(MediaType.APPLICATION_JSON)
                        .header(UserContextHeaders.USER_EXTERNAL_ID,UUID.randomUUID())
                        .header(UserContextHeaders.USER_NAME,"Test User")
                        .header(UserContextHeaders.USER_EMAIL,"User@test.com")
                        .header(UserContextHeaders.AUTH_PROVIDER,"GOOGLE")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Truth analysis request created successfully"))
                .andExpect(jsonPath("$.data.status").value("RECEIVED"));
    }

    @Test
    void shouldAnalyzeTruthSuccessfully() throws Exception{

        //Given
        TruthAnalysisResponse response = sampleResponse("ANALYZED");
        AnalyzeTruthRequest request = new AnalyzeTruthRequest(
                "Online claim about Bürgergeld",
                "Everyone can get Bürgergeld without checks."
        );

        //when
        when(service.analyzeTruth(any(AnalyzeTruthRequest.class),any(AuthenticatedUserContext.class))).thenReturn(response);

        mockMvc.perform(post("/api/truth/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                        .header(UserContextHeaders.USER_EXTERNAL_ID,UUID.randomUUID())
                        .header(UserContextHeaders.USER_NAME,"Test User")
                        .header(UserContextHeaders.USER_EMAIL,"User@test.com")
                        .header(UserContextHeaders.AUTH_PROVIDER,"GOOGLE")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Truth analysis completed successfully"))
                .andExpect(jsonPath("$.data.status").value("ANALYZED"))
                .andExpect(jsonPath("$.data.trustScore").value(35));
    }

    @Test
    void shouldReturnBadRequestForInvalidAnalyzeRequest() throws Exception {
        //Given
        AnalyzeTruthRequest request = new AnalyzeTruthRequest("", "");

        mockMvc.perform(post("/api/truth/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                        .header(UserContextHeaders.USER_EXTERNAL_ID,UUID.randomUUID())
                        .header(UserContextHeaders.USER_NAME,"Test User")
                        .header(UserContextHeaders.USER_EMAIL,"User@test.com")
                        .header(UserContextHeaders.AUTH_PROVIDER,"GOOGLE")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.fieldErrors").isArray());
    }

    @Test
    void shouldReturnRecentTruthAnalyses() throws Exception {
        when(service.getRecentAnalyses(any(AuthenticatedUserContext.class)))
                .thenReturn(List.of(sampleResponse("ANALYZED")));

        mockMvc.perform(get("/api/truth")
                        .header(UserContextHeaders.USER_EXTERNAL_ID,UUID.randomUUID())
                        .header(UserContextHeaders.USER_NAME,"Test User")
                        .header(UserContextHeaders.USER_EMAIL,"User@test.com")
                        .header(UserContextHeaders.AUTH_PROVIDER,"GOOGLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].status").value("ANALYZED"));
    }

    @Test
    void shouldReturnTruthAnalysisById() throws Exception {
        UUID id = UUID.randomUUID();

        when(service.getAnalysisById(id))
                .thenReturn(sampleResponse(id, "ANALYZED"));

        mockMvc.perform(get("/api/truth/{id}", id)
                        .header(UserContextHeaders.USER_EXTERNAL_ID,UUID.randomUUID())
                        .header(UserContextHeaders.USER_NAME,"Test User")
                        .header(UserContextHeaders.USER_EMAIL,"User@test.com")
                        .header(UserContextHeaders.AUTH_PROVIDER,"GOOGLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(id.toString()));
    }



    private TruthAnalysisResponse sampleResponse(String status) {
        return sampleResponse(UUID.randomUUID(), status);
    }

    private TruthAnalysisResponse sampleResponse(UUID id, String status) {
        return new TruthAnalysisResponse(
                id,
                "Online claim about Bürgergeld",
                "Everyone can get Bürgergeld without checks.",
                "Claim says Bürgergeld has no eligibility checks.",
                35,
                "HIGH",
                "This is risky because Bürgergeld normally has eligibility checks.",
                "Check official Jobcenter sources.",
                status,
                Instant.now()
        );
    }


}