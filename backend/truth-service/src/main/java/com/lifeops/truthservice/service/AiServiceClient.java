package com.lifeops.truthservice.service;

import com.lifeops.truthservice.dto.TruthAnalysisResult;
import com.lifeops.truthservice.dto.ai.AiApiResponse;
import com.lifeops.truthservice.dto.ai.AiTruthAnalysisRequest;
import com.lifeops.truthservice.dto.ai.AiTruthAnalysisResponse;
import com.lifeops.truthservice.exception.TruthAnalysisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AiServiceClient {
    private final static Logger log = LoggerFactory.getLogger(AiServiceClient.class);

    private final RestClient restClient;

    public AiServiceClient(RestClient.Builder restClientBuilder, @Value("${lifeops.services.ai-service-url}") String aiServiceUrl) {
        this.restClient = restClientBuilder
                .baseUrl(aiServiceUrl)
                .build();
    }

    public TruthAnalysisResult analyzeTruth(String title, String content){
        try {
            AiApiResponse<AiTruthAnalysisResponse> response = restClient.post()
                    .uri("/api/ai/truth-analysis")
                    .body(new AiTruthAnalysisRequest(title, content))
                    .retrieve()
                    .body(new ParameterizedTypeReference<>(){});

            if(response == null || response.data() == null){
                throw new TruthAnalysisException("AI truth analysis response is empty");
            }

            AiTruthAnalysisResponse analysis = response.data();

            return new TruthAnalysisResult(
                    analysis.claimSummary(),
                    analysis.trustScore(),
                    analysis.riskLevel(),
                    analysis.explanation(),
                    analysis.suggestedVerificationSteps()
            );

        }catch (TruthAnalysisException exception) {
            throw exception;
        } catch (Exception exception) {
            log.error("Truth analysis call to ai-service failed", exception);
            throw new TruthAnalysisException("Unable to analyze content credibility using AI", exception);
        }
    }
}
