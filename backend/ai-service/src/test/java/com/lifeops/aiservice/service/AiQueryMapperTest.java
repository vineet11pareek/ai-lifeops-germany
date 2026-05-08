package com.lifeops.aiservice.service;


import com.lifeops.aiservice.dto.AiQueryHistoryResponse;
import com.lifeops.aiservice.entity.AiQuery;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AiQueryMapperTest {

    private final AiQueryMapper aiQueryMapper = new AiQueryMapper();

    @Test
    void shouldMapAiQueryToHistoryResponse(){
        AiQuery aiQuery = new AiQuery(
                null,
                "Explain Anmeldung"
        );

        aiQuery.markProcessing();
        aiQuery.markCompleted(
                "Anmeldung means address registration.",
                "OPENAI",
                "gpt-4o-mini"
        );

        AiQueryHistoryResponse response = aiQueryMapper.toHistoryResponse(aiQuery);

        assertThat(response.question()).isEqualTo("Explain Anmeldung");
        assertThat(response.answer()).isEqualTo("Anmeldung means address registration.");
        assertThat(response.status()).isEqualTo("COMPLETED");
        assertThat(response.provider()).isEqualTo("OPENAI");
        assertThat(response.model()).isEqualTo("gpt-4o-mini");

    }
}
