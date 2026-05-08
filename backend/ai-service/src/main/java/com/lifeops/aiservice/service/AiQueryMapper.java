package com.lifeops.aiservice.service;

import com.lifeops.aiservice.dto.AiChatResponse;
import com.lifeops.aiservice.dto.AiQueryHistoryResponse;
import com.lifeops.aiservice.entity.AiQuery;
import org.springframework.stereotype.Component;

@Component
public class AiQueryMapper {

    public AiChatResponse toChatResponse(AiQuery aiQuery) {
        return new AiChatResponse(
                aiQuery.getId(),
                aiQuery.getQuestion(),
                aiQuery.getAnswer(),
                aiQuery.getStatus().name(),
                aiQuery.getProvider(),
                aiQuery.getModel(),
                aiQuery.getCreatedAt()
        );
    }

    public AiQueryHistoryResponse toHistoryResponse(AiQuery aiQuery) {
        return new AiQueryHistoryResponse(
                aiQuery.getId(),
                aiQuery.getQuestion(),
                aiQuery.getAnswer(),
                aiQuery.getStatus().name(),
                aiQuery.getProvider(),
                aiQuery.getModel(),
                aiQuery.getCreatedAt()
        );
    }
}
