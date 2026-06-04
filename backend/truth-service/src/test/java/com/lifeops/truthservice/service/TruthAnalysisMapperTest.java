package com.lifeops.truthservice.service;

import com.lifeops.truthservice.dto.TruthAnalysisResponse;
import com.lifeops.truthservice.entity.RiskLevel;
import com.lifeops.truthservice.entity.TruthAnalysis;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TruthAnalysisMapperTest {


    private final TruthAnalysisMapper truthAnalysisMapper = new TruthAnalysisMapper();

    @Test
    public void shouldMapTruthAnalysisToResponse(){
        //Given
        TruthAnalysis analysis = new TruthAnalysis(
                null,
                "google-sub-123",
                "Online claim about Bürgergeld",
                "Everyone can get Bürgergeld without checks."
        );

        analysis.markAnalyzed(
                "Claim says Bürgergeld has no eligibility checks.",
                35,
                RiskLevel.HIGH,
                "This is risky because Bürgergeld normally has eligibility checks.",
                "Check official Agentur für Arbeit or Jobcenter sources."
        );

        //When
        TruthAnalysisResponse response = truthAnalysisMapper.toResponse(analysis);

        //then
        assertNotNull(response);
        assertThat(response.status()).isEqualTo("ANALYZED");
        assertThat(response.title()).isEqualTo("Online claim about Bürgergeld");
        assertThat(response.trustScore()).isEqualTo(35);
        assertThat(response.riskLevel()).isEqualTo("HIGH");

    }

}