package com.lifeops.documentservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifeops.documentservice.dto.DocumentAnalysisResult;
import com.lifeops.documentservice.dto.ai.AiApiResponse;
import com.lifeops.documentservice.dto.ai.AiChatRequest;
import com.lifeops.documentservice.dto.ai.AiChatResponse;
import com.lifeops.documentservice.exception.DocumentAnalysisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AiServiceClient {
    private static final Logger log = LoggerFactory.getLogger(AiServiceClient.class);

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public AiServiceClient(RestClient.Builder restClientBuilder, ObjectMapper objectMapper,
                           @Value("${lifeops.services.ai-service-url}") String aiServiceUrl) {
        this.restClient = restClientBuilder.baseUrl(aiServiceUrl).build();
        this.objectMapper = objectMapper;
    }

    public DocumentAnalysisResult analyzeDocument(String title, String content){
        String prompt = buildPrompt(title,content);
        try {
            AiApiResponse<AiChatResponse> response = restClient
                    .post()
                    .uri("/api/ai/chat")
                    .body(new AiChatRequest(prompt))
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

            if(response==null || response.data()==null || response.data().answer()==null){
                throw new DocumentAnalysisException("AI analysis response is empty");
            }
            return parseAnalysis(response.data().answer());
        } catch (DocumentAnalysisException exception) {
            throw exception;
        } catch (Exception exception) {
            log.error("Document analysis call to ai-service failed",exception);
            throw new DocumentAnalysisException("Unable to analyze document using AI", exception);
        }
    }

    private String buildPrompt(String title, String content) {
        return """
                Analyze the following German/EU document for a user living in Germany.

                Return ONLY valid JSON with these exact fields:
                {
                  "summary": "...",
                  "deadlineText": "...",
                  "requiredAction": "...",
                  "riskLevel": "LOW|MEDIUM|HIGH|UNKNOWN",
                  "suggestedNextStep": "..."
                }

                Rules:
                - Use simple English.
                - If no deadline is found, use "No clear deadline found".
                - If no required action is found, use "No clear action required".
                - riskLevel must be only LOW, MEDIUM, HIGH, or UNKNOWN.
                - Do not include markdown.
                - Do not include explanation outside JSON.

                Document title:
                %s

                Document content:
                %s
                """.formatted(title, content);
    }

    private DocumentAnalysisResult parseAnalysis(String answer){
        try{
            return objectMapper.readValue(answer,DocumentAnalysisResult.class);
        }catch (Exception exception){
            log.error("Failed to parse AI document analysis JSON. Raw answer: {}",answer);
            throw new DocumentAnalysisException("AI returned an invalid document analysis format", exception);
        }
    }


}
