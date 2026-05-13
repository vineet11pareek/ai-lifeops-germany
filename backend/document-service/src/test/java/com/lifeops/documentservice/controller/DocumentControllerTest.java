package com.lifeops.documentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lifeops.documentservice.dto.AnalyzeDocumentRequest;
import com.lifeops.documentservice.dto.CreateDocumentRequest;
import com.lifeops.documentservice.dto.DocumentResponse;
import com.lifeops.documentservice.service.DocumentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DocumentController.class)
class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DocumentService documentService;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Test
    void shouldCreateDocumentSuccessfully() throws Exception {
        DocumentResponse response = new DocumentResponse(
                UUID.randomUUID(),
                "Letter from Finanzamt",
                "Please submit missing documents.",
                null,
                null,
                null,
                "UNKNOWN",
                null,
                "RECEIVED",
                Instant.now()
        );

        when(documentService.createDocument(any(CreateDocumentRequest.class)))
                .thenReturn(response);

        CreateDocumentRequest request = new CreateDocumentRequest(
                "Letter from Finanzamt",
                "Please submit missing documents."
        );

        mockMvc.perform(post("/api/documents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Document created successfully"))
                .andExpect(jsonPath("$.data.title").value("Letter from Finanzamt"))
                .andExpect(jsonPath("$.data.status").value("RECEIVED"));
    }

    @Test
    void shouldAnalyzeDocumentSuccessfully() throws Exception {
        DocumentResponse response = new DocumentResponse(
                UUID.randomUUID(),
                "Letter from Finanzamt",
                "Please submit missing documents by 15.06.2026.",
                "The letter asks for missing documents.",
                "15.06.2026",
                "Submit missing documents.",
                "MEDIUM",
                "Prepare documents and send them before the deadline.",
                "ANALYZED",
                Instant.now()
        );

        when(documentService.analyzeDocument(any(AnalyzeDocumentRequest.class)))
                .thenReturn(response);

        AnalyzeDocumentRequest request = new AnalyzeDocumentRequest(
                "Letter from Finanzamt",
                "Please submit missing documents by 15.06.2026."
        );

        mockMvc.perform(post("/api/documents/analyze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Document analyzed successfully"))
                .andExpect(jsonPath("$.data.status").value("ANALYZED"))
                .andExpect(jsonPath("$.data.riskLevel").value("MEDIUM"));
    }

    @Test
    void shouldReturnBadRequestForInvalidAnalyzeRequest() throws Exception {
        AnalyzeDocumentRequest request = new AnalyzeDocumentRequest(
                "",
                ""
        );

        mockMvc.perform(post("/api/documents/analyze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.fieldErrors").isArray());
    }

    @Test
    void shouldReturnDocumentById() throws Exception {
        UUID documentId = UUID.randomUUID();

        DocumentResponse response = new DocumentResponse(
                documentId,
                "Letter from Finanzamt",
                "Please submit missing documents.",
                null,
                null,
                null,
                "UNKNOWN",
                null,
                "RECEIVED",
                Instant.now()
        );

        when(documentService.getDocumentById(documentId)).thenReturn(response);

        mockMvc.perform(get("/api/documents/{id}", documentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(documentId.toString()))
                .andExpect(jsonPath("$.data.title").value("Letter from Finanzamt"));
    }

}