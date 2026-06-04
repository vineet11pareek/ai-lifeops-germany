package com.lifeops.aiservice.service;

import com.lifeops.aiservice.dto.AiQueryHistoryResponse;
import com.lifeops.aiservice.dto.AuthenticatedUserContext;
import com.lifeops.aiservice.entity.AiQuery;
import com.lifeops.aiservice.repository.AiQueryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AiChatServiceTest {
    @Mock
    private AiQueryRepository aiQueryRepository;

    @Mock
    private ChatClient.Builder chatClientBuilder;

    @Mock
    private ChatClient chatClient;

    @Mock
    private AiQueryEventPublisher aiQueryEventPublisher;

    @Mock
    private AiQueryMapper aiQueryMapper;

    private AiChatService aiChatService;

    @BeforeEach
    void setUp() {
        when(chatClientBuilder.build()).thenReturn(chatClient);

        aiChatService = new AiChatService(
                chatClientBuilder,
                "gpt-4o-mini",
                aiQueryRepository,
                aiQueryEventPublisher,
                aiQueryMapper
        );
    }

    @Test
    void shouldReturnRecentQueries(){
        //Given
        String userExternalId = "107691503500061573151129";
        AiQuery aiQuery = new AiQuery(
                userExternalId,
                null,
                "Explain Anmeldung in Germany"
        );
        AuthenticatedUserContext userContext = new AuthenticatedUserContext(
                "google-sub-123",
                "test@example.com",
                "Test User",
                "GOOGLE"
        );

        aiQuery.markProcessing();
        aiQuery.markCompleted(
                "Anmeldung is address registration in Germany",
                "OPENAI",
                "gpt-4o-mini"
        );
        AiQueryHistoryResponse historyResponse = new AiQueryHistoryResponse(
                aiQuery.getId(),
                aiQuery.getQuestion(),
                aiQuery.getAnswer(),
                aiQuery.getStatus().toString(),
                aiQuery.getProvider(),
                aiQuery.getModel(),
                aiQuery.getCreatedAt()
        );

        when(aiQueryMapper.toHistoryResponse(aiQuery))
                .thenReturn(historyResponse);

        when(aiQueryRepository.findTop20ByUserExternalIdOrderByCreatedAtDesc(any(String.class)))
                .thenReturn(List.of(aiQuery));



        List<AiQueryHistoryResponse> history = aiChatService.getRecentQueries(userContext);
        verify(aiQueryRepository).findTop20ByUserExternalIdOrderByCreatedAtDesc(userContext.externalId());
        assertThat(history).hasSize(1);
        assertThat(history.get(0).question()).isEqualTo("Explain Anmeldung in Germany");
        assertThat(history.get(0).status()).isEqualTo("COMPLETED");
        assertThat(history.get(0).provider()).isEqualTo("OPENAI");
    }
}
