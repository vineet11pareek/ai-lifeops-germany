package com.lifeops.aiservice.service;

import com.lifeops.aiservice.dto.AiQueryHistoryResponse;
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
        AiQuery aiQuery = new AiQuery(
                null,
                "Explain Anmeldung in Germany"
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

        when(aiQueryRepository.findTop20ByOrderByCreatedAtDesc())
                .thenReturn(List.of(aiQuery));



        List<AiQueryHistoryResponse> history = aiChatService.getRecentQueries();
        verify(aiQueryRepository).findTop20ByOrderByCreatedAtDesc();
        assertThat(history).hasSize(1);
        assertThat(history.get(0).question()).isEqualTo("Explain Anmeldung in Germany");
        assertThat(history.get(0).status()).isEqualTo("COMPLETED");
        assertThat(history.get(0).provider()).isEqualTo("OPENAI");
    }
}
