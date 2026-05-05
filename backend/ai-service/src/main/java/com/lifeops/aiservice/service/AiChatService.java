package com.lifeops.aiservice.service;

import com.lifeops.aiservice.dto.AiChatResponse;
import com.lifeops.aiservice.dto.AiQueryHistoryResponse;
import com.lifeops.aiservice.entity.AiQuery;
import com.lifeops.aiservice.exception.AiProcessingException;
import com.lifeops.aiservice.repository.AiQueryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AiChatService {

    private static Logger log = LoggerFactory.getLogger(AiChatService.class);

    private final ChatClient chatClient;
    private final String model;
    private final AiQueryRepository aiQueryRepository;

    public AiChatService(ChatClient.Builder chatClientBuilder,
                         @Value("${spring.ai.openai.chat.options.model}") String model,
                         AiQueryRepository aiQueryRepository){
        this.chatClient = chatClientBuilder.build();
        this.model = model;
        this.aiQueryRepository=aiQueryRepository;
    }


    @Transactional
    public AiChatResponse ask(String question){

        log.info("Creating AI query record");

        AiQuery aiQuery = new AiQuery(null,question);
        aiQuery.markProcessing();
        aiQueryRepository.saveAndFlush(aiQuery);


        try {
            log.info("Processing AI chat request using model={}", model);
            String answer = chatClient.prompt()
                    .system("""
                            You are AI LifeOps Assistant.
                            Answer clearly, practically, and safely.
                            If the user asks legal, tax, medical, or financial questions,
                            explain that the answer is guidance, not professional advice.
                            """)
                    .user(question)
                    .call()
                    .content();

            aiQuery.markCompleted(answer,"OPENAI",model);
            AiQuery savedQuery = aiQueryRepository.saveAndFlush(aiQuery);
            log.info("AI chat request completed successfully queryId={}", savedQuery.getId());

            return toChatResponse(savedQuery);

        } catch (Exception exception) {
            log.error("AI chat request failed queryId={}", aiQuery.getId(),exception);
            aiQuery.markFailed("AI provider is temporarily unavailable");
            aiQueryRepository.save(aiQuery);

            throw new AiProcessingException(
                    "AI provider is temporarily unavailable. Please try again later.",
                    exception
            );
        }
    }

    @Transactional(readOnly = true)
    public List<AiQueryHistoryResponse> getRecentQueries(){
        return aiQueryRepository.findTop20ByOrderByCreatedAtDesc()
                .stream()
                .map(this::toHistoryResponse )
                .toList();
    }

    private AiChatResponse toChatResponse(AiQuery aiQuery) {
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

    private AiQueryHistoryResponse toHistoryResponse(AiQuery aiQuery) {
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
