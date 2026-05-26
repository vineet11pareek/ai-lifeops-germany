package com.lifeops.aiservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifeops.aiservice.dto.TruthAnalysisRequest;
import com.lifeops.aiservice.dto.TruthAnalysisResponse;
import com.lifeops.aiservice.exception.AiProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TruthAnalysisService {

    private final static Logger log = LoggerFactory.getLogger(TruthAnalysisService.class);

    private final ObjectMapper objectMapper;
    private final ChatClient chatClient;
    private final String model;

    public TruthAnalysisService(ObjectMapper objectMapper, ChatClient.Builder chatClientBuilder, @Value("${spring.ai.openai.chat.options.model}") String model) {
        this.objectMapper = objectMapper;
        this.chatClient = chatClientBuilder.build();
        this.model = model;
    }

    public TruthAnalysisResponse analyze(String title, String content){
        log.info("Starting truth analysis using model={}", model);
        try{
            String rawResponse = chatClient
                    .prompt()
                    .system("""
                            You are the Truth Layer assistant for AI LifeOps Germany Edition.

                            Your task:
                            Analyze pasted online content or claims for credibility risk.
                            You are not an absolute fact authority.
                            You help the user understand what claims are being made,
                            how risky or questionable they appear, and how to verify them.

                            Return ONLY valid JSON with these exact fields:
                            {
                              "claimSummary": "...",
                              "trustScore": 0,
                              "riskLevel": "LOW|MEDIUM|HIGH|UNKNOWN",
                              "explanation": "...",
                              "suggestedVerificationSteps": "..."
                            }

                            Rules:
                            - Use simple English.
                            - trustScore must be an integer between 0 and 100.
                            - 0 means very unreliable.
                            - 50 means uncertain or mixed.
                            - 100 means highly reliable.
                            - riskLevel must be only LOW, MEDIUM, HIGH, or UNKNOWN.
                            - Do not say something is 100% true or false.
                            - Mention when external verification is needed.
                            - For legal, tax, medical, political, financial, or benefit-related claims,
                              recommend checking official sources.
                            - Do not include markdown.
                            - Do not include explanation outside JSON.
                            """)
                    .user("""
                            Content title:
                            %s

                            Content to analyze:
                            %s
                            """.formatted(title, content))
                    .call()
                    .content();

            TruthAnalysisResponse response = objectMapper.readValue(rawResponse, TruthAnalysisResponse.class);
            log.info("Truth analysis completed successfully");
            return normalize(response);
        } catch (Exception exception) {
            log.error("Truth analysis failed ", exception);
            throw new AiProcessingException(
                    "Unable to analyze content credibility using AI. Please try again later.",
                    exception
            );
        }
    }

    private TruthAnalysisResponse normalize(TruthAnalysisResponse response) {
        return new TruthAnalysisResponse(
                defaultValue(response.claimSummary(), "No clear claim summary available"),
                normalizeTrustScore(response.trustScore()),
                normalizeRiskLevel(response.riskLevel()),
                defaultValue(response.explanation(), "No explanation available"),
                defaultValue(response.suggestedVerificationSteps(), "Check official or trusted sources before acting.")
        );
    }

    private Integer normalizeTrustScore(Integer score) {
        if (score == null) {
            return 50;
        }

        if (score < 0) {
            return 0;
        }

        if (score > 100) {
            return 100;
        }

        return score;
    }

    private String normalizeRiskLevel(String riskLevel) {
        if (riskLevel == null) {
            return "UNKNOWN";
        }

        return switch (riskLevel.toUpperCase()) {
            case "LOW", "MEDIUM", "HIGH", "UNKNOWN" -> riskLevel.toUpperCase();
            default -> "UNKNOWN";
        };
    }

    private String defaultValue(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }

        return value;
    }
}
