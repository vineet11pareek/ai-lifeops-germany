package com.lifeops.truthservice.service;

import com.lifeops.truthservice.dto.*;
import com.lifeops.truthservice.entity.TruthAnalysis;
import com.lifeops.truthservice.event.TruthAnalyzedEvent;
import com.lifeops.truthservice.repository.TruthAnalysisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TruthAnalysisServiceTest {

    @Mock
    private TruthAnalysisRepository truthAnalysisRepository;

    @Mock
    private TruthAnalysisMapper truthAnalysisMapper;

    @Mock
    private AiServiceClient aiServiceClient;

    @Mock
    private TruthEventPublisher truthEventPublisher;

    @InjectMocks
    private TruthAnalysisService service;

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
    void shouldCreateTruthAnalysisRequest(){
        //Given
        CreateTruthAnalysisRequest request = new CreateTruthAnalysisRequest(
                "Online claim about Bürgergeld",
                "Everyone can get Bürgergeld without checks."
        );

        TruthAnalysis saved = new TruthAnalysis(null,"google-sub-123", request.title(),request.content());

        TruthAnalysisResponse expected = new TruthAnalysisResponse(
                saved.getId(),
                saved.getTitle(),
                saved.getContent(),
                null,
                null,
                "UNKNOWN",
                null,
                null,
                "RECEIVED",
                saved.getCreatedAt()
        );

        //when
        when(truthAnalysisRepository.save(any(TruthAnalysis.class))).thenReturn(saved);
        when(truthAnalysisMapper.toResponse(saved)).thenReturn(expected);

        TruthAnalysisResponse response = service.createTruthAnalysis(request,userContext);

        //then
        assertThat(response.status()).isEqualTo("RECEIVED");
        assertThat(response.riskLevel()).isEqualTo("UNKNOWN");

        verify(truthAnalysisRepository,times(1)).save(any(TruthAnalysis.class));
        verifyNoInteractions(aiServiceClient);
        verifyNoInteractions(truthEventPublisher);

    }

    @Test
    void shouldAnalyzedTruthAndPublishedEvent(){

        //Given
        AnalyzeTruthRequest request = new AnalyzeTruthRequest(
                "Online claim about Bürgergeld",
                "Everyone can get Bürgergeld without checks."
        );

        TruthAnalysisResult result = new TruthAnalysisResult(
                "Claim says Bürgergeld has no eligibility checks.",
                35,
                "HIGH",
                "This is risky because Bürgergeld normally has eligibility checks.",
                "Check official Jobcenter sources."
        );

        //When
        when(truthAnalysisRepository.save(any(TruthAnalysis.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(aiServiceClient.analyzeTruth(request.title(),request.content())).thenReturn(result);

        when(truthAnalysisMapper.toResponse(any(TruthAnalysis.class)))
                .thenAnswer(invocation -> {
                    TruthAnalysis analysis = invocation.getArgument(0);
                    return new TruthAnalysisResponse(
                            analysis.getId(),
                            analysis.getTitle(),
                            analysis.getContent(),
                            analysis.getClaimSummary(),
                            analysis.getTrustScore(),
                            analysis.getRiskLevel().name(),
                            analysis.getExplanation(),
                            analysis.getSuggestedVerificationSteps(),
                            analysis.getStatus().name(),
                            analysis.getCreatedAt()
                    );
                });

        TruthAnalysisResponse response = service.analyzeTruth(request,userContext);

        assertThat(response.status()).isEqualTo("ANALYZED");
        assertThat(response.trustScore()).isEqualTo(35);
        assertThat(response.riskLevel()).isEqualTo("HIGH");

        ArgumentCaptor<TruthAnalyzedEvent> eventCaptor = ArgumentCaptor.forClass(TruthAnalyzedEvent.class);
        verify(truthEventPublisher).publish(eventCaptor.capture());

        TruthAnalyzedEvent event = eventCaptor.getValue();

        assertThat(event.analysisId()).isNotNull();
        assertThat(event.title()).isEqualTo("Online claim about Bürgergeld");
        assertThat(event.trustScore()).isEqualTo(35);
        assertThat(event.riskLevel()).isEqualTo("HIGH");

    }

    @Test
    void shouldReturnRecentAnalyses(){

        //Given
        TruthAnalysis analysis = new TruthAnalysis(
                null,
                "google-sub-123",
                "Online claim about Bürgergeld",
                "Everyone can get Bürgergeld without checks."
        );

        TruthAnalysisResponse expected = new TruthAnalysisResponse(
                analysis.getId(),
                analysis.getTitle(),
                analysis.getContent(),
                null,
                null,
                "UNKNOWN",
                null,
                null,
                "RECEIVED",
                analysis.getCreatedAt()
        );

        //When
        when(truthAnalysisRepository.findTop20ByUserExternalIdOrderByCreatedAtDesc(any(String.class))).thenReturn(List.of(analysis));
        when(truthAnalysisMapper.toResponse(analysis)).thenReturn(expected);

        List<TruthAnalysisResponse> response = service.getRecentAnalyses(userContext);

        //Then
        assertThat(response).hasSize(1);
        assertThat(response.get(0).title()).isEqualTo("Online claim about Bürgergeld");

    }

    @Test
    void shouldReturnAnalysisById() {
        UUID id = UUID.randomUUID();

        TruthAnalysis analysis = new TruthAnalysis(
                null,
                "google-sub-123",
                "Online claim about Bürgergeld",
                "Everyone can get Bürgergeld without checks."
        );

        TruthAnalysisResponse expected = new TruthAnalysisResponse(
                analysis.getId(),
                analysis.getTitle(),
                analysis.getContent(),
                null,
                null,
                "UNKNOWN",
                null,
                null,
                "RECEIVED",
                analysis.getCreatedAt()
        );

        when(truthAnalysisRepository.findById(id)).thenReturn(Optional.of(analysis));
        when(truthAnalysisMapper.toResponse(analysis)).thenReturn(expected);

        TruthAnalysisResponse response = service.getAnalysisById(id);
        assertThat(response.title()).isEqualTo("Online claim about Bürgergeld");
    }

}