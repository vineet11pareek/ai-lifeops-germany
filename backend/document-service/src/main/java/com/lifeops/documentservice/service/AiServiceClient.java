package com.lifeops.documentservice.service;

import com.lifeops.documentservice.dto.DocumentAnalysisResult;
import com.lifeops.documentservice.dto.ai.AiApiResponse;
import com.lifeops.documentservice.dto.ai.AiDocumentAnalysisRequest;
import com.lifeops.documentservice.dto.ai.AiDocumentAnalysisResponse;
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

    public AiServiceClient(
            RestClient.Builder restClientBuilder,
            @Value("${lifeops.services.ai-service-url}") String aiServiceUrl
    ) {
        this.restClient = restClientBuilder
                .baseUrl(aiServiceUrl)
                .build();
    }

    public DocumentAnalysisResult analyzeDocument(String title, String content) {
        try {
            AiApiResponse<AiDocumentAnalysisResponse> response = restClient.post()
                    .uri("/api/ai/document-analysis")
                    .body(new AiDocumentAnalysisRequest(title, content))
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

            if (response == null || response.data() == null) {
                throw new DocumentAnalysisException("AI analysis response is empty");
            }

            AiDocumentAnalysisResponse analysis = response.data();

            return new DocumentAnalysisResult(
                    analysis.summary(),
                    analysis.deadlineText(),
                    analysis.requiredAction(),
                    analysis.riskLevel(),
                    analysis.suggestedNextStep()
            );
        } catch (DocumentAnalysisException exception) {
            throw exception;
        } catch (Exception exception) {
            log.error("Document analysis call to ai-service failed", exception);
            throw new DocumentAnalysisException("Unable to analyze document using AI", exception);
        }
    }
}