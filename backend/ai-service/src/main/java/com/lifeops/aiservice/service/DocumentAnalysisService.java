package com.lifeops.aiservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifeops.aiservice.dto.DocumentAnalysisResponse;
import com.lifeops.aiservice.exception.AiProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DocumentAnalysisService {

    private final static Logger log = LoggerFactory.getLogger(DocumentAnalysisService.class);
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;
    private final String model;

    public DocumentAnalysisService(ChatClient.Builder chatClientBuilder, ObjectMapper objectMapper, @Value("${spring.ai.openai.chat.options.model}") String model) {
        this.chatClient = chatClientBuilder.build();
        this.objectMapper = objectMapper;
        this.model = model;
    }

    public DocumentAnalysisResponse analyze(String title, String content) {
        log.info("Starting document analyzing using model: {}", model);
        try {
            String rawResponse = chatClient
                    .prompt()
                    .system("""
                            You are an AI document analysis assistant for users living in Germany.
                            
                                                        Your task:
                                                        Analyze German/EU administrative letters, contracts, and official documents.
                            
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
                                                        - Do not include markdown.
                                                        - Do not include explanations outside JSON.
                                                        - If no deadline is found, use "No clear deadline found".
                                                        - If no required action is found, use "No clear action required".
                                                        - riskLevel must be only LOW, MEDIUM, HIGH, or UNKNOWN.
                                                        - Do not give legal advice. Give practical guidance only.
                            """)
                    .user("""
                             Document title:
                                                        %s
                            
                                                        Document content:
                                                        %s
                            """.formatted(title,content))
                    .call()
                    .content();

            DocumentAnalysisResponse response = objectMapper.readValue(rawResponse, DocumentAnalysisResponse.class);
            log.info("Document analysis completed successfully");
            return normalize(response);
        } catch (Exception exception) {
            log.error("Document analysis failed",exception);
            throw new AiProcessingException(
                    "Unable to analyze document using AI. Please try again",
                    exception
            );
        }
    }

    private DocumentAnalysisResponse normalize(DocumentAnalysisResponse response){
        return new DocumentAnalysisResponse(
                defaultValue(response.summary(),"No summary available"),
                defaultValue(response.deadlineText(),"No clear deadline available"),
                defaultValue(response.requiredAction(),"No clear action required"),
                normalizeRiskLevel(response.riskLevel()),
                defaultValue(response.suggestedNextStep(),"Review the document carefully and take action if needed.")
        );
    }

    private String normalizeRiskLevel(String riskLevel){
        if(riskLevel==null){
            return "UNKNOWN";
        }
        return switch (riskLevel){
            case "LOW","MEDIUM","HIGH","UNKNOWN" -> riskLevel.toUpperCase();
            default -> "UNKNOWN";
        };
    }

    private String defaultValue(String value, String fallback){
        if(value==null || value.isBlank()){
            return fallback;
        }
        return value;
    }
}
