package com.lifeops.truthservice.service;

import com.lifeops.truthservice.dto.TruthAnalysisResponse;
import com.lifeops.truthservice.entity.TruthAnalysis;
import org.springframework.stereotype.Component;

@Component
public class TruthAnalysisMapper {

    public TruthAnalysisResponse toResponse(TruthAnalysis truthAnalysis){
        return new TruthAnalysisResponse(
                truthAnalysis.getId(),
                truthAnalysis.getTitle(),
                truthAnalysis.getContent(),
                truthAnalysis.getClaimSummary(),
                truthAnalysis.getTrustScore(),
                truthAnalysis.getRiskLevel() != null ? truthAnalysis.getRiskLevel().name() : null,
                truthAnalysis.getExplanation(),
                truthAnalysis.getSuggestedVerificationSteps(),
                truthAnalysis.getStatus().name(),
                truthAnalysis.getCreatedAt()
        );
    }
}
