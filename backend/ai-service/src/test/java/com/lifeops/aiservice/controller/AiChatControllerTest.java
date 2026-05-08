package com.lifeops.aiservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lifeops.aiservice.dto.AiChatRequest;
import com.lifeops.aiservice.dto.AiChatResponse;
import com.lifeops.aiservice.dto.AiQueryHistoryResponse;
import com.lifeops.aiservice.service.AiChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AiChatController.class)
public class AiChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AiChatService aiChatService;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Test
    void shouldAskAiSuccessfully() throws Exception{
        AiChatResponse response = new AiChatResponse(
                UUID.randomUUID(),
                "Explain Anmeldung",
                "Anmeldung means address registration",
                "COMPLETED",
                "OPENAI",
                "gpt-4o-mini",
                Instant.now()
        );

        when(aiChatService.ask(anyString())).thenReturn(response);

        AiChatRequest request = new AiChatRequest("Explain Anmeldung");

        mockMvc.perform(post("/api/ai/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("AI response generated successfully"))
                .andExpect(jsonPath("$.data.question").value("Explain Anmeldung"))
                .andExpect(jsonPath("$.data.status").value("COMPLETED"));
    }

    @Test
    void shouldReturnBadRequestForEmptyQuestions() throws Exception{
        AiChatRequest request = new AiChatRequest("");

        mockMvc.perform(post("/api/ai/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.fieldErrors").isArray());
    }

    @Test
    void shouldReturnQueryHistory() throws Exception{
        AiQueryHistoryResponse history = new AiQueryHistoryResponse(
                UUID.randomUUID(),
                "Explain Anmeldung",
                "Anmeldung means address registration.",
                "COMPLETED",
                "OPENAI",
                "gpt-4o-mini",
                Instant.now()
        );

        when(aiChatService.getRecentQueries()).thenReturn(List.of(history));

        mockMvc.perform(get("/api/ai/queries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].question").value("Explain Anmeldung"))
                .andExpect(jsonPath("$.data[0].status").value("COMPLETED"));
    }
}
