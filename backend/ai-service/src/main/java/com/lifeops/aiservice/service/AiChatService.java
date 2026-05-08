package com.lifeops.aiservice.service;

import com.lifeops.aiservice.dto.AiChatResponse;
import com.lifeops.aiservice.dto.AiQueryHistoryResponse;
import com.lifeops.aiservice.entity.AiQuery;
import com.lifeops.aiservice.event.AiQueryCompletedEvent;
import com.lifeops.aiservice.exception.AiProcessingException;
import com.lifeops.aiservice.repository.AiQueryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AiChatService {

    private static Logger log = LoggerFactory.getLogger(AiChatService.class);

    private final ChatClient chatClient;
    private final AiQueryMapper aiQueryMapper;
    private final String model;
    private final AiQueryRepository aiQueryRepository;
    private final AiQueryEventPublisher aiQueryEventPublisher;

    public AiChatService(ChatClient.Builder chatClientBuilder,
                         @Value("${spring.ai.openai.chat.options.model}") String model,
                         AiQueryRepository aiQueryRepository,
                         AiQueryEventPublisher aiQueryEventPublisher,
                         AiQueryMapper aiQueryMapper){
        this.chatClient = chatClientBuilder.build();
        this.model = model;
        this.aiQueryRepository=aiQueryRepository;
        this.aiQueryEventPublisher=aiQueryEventPublisher;
        this.aiQueryMapper=aiQueryMapper;
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
                            You are AI LifeOps Assistant for Germany.
                            Answer clearly, practically, and safely.
                            If the user asks legal, tax, medical, or financial questions,
                            explain that the answer is guidance, not professional advice.
                            """)
                    .user(question)
                    .call()
                    .content();

            aiQuery.markCompleted(answer,"OPENAI",model);
            AiQuery savedQuery = aiQueryRepository.saveAndFlush(aiQuery);
            aiQueryEventPublisher.publish(new AiQueryCompletedEvent(
                    UUID.randomUUID(),
                    savedQuery.getId(),
                    savedQuery.getUserId(),
                    savedQuery.getQuestion(),
                    savedQuery.getStatus().name(),
                    savedQuery.getProvider(),
                    savedQuery.getModel(),
                    Instant.now()));
            log.info("AI chat request completed successfully queryId={}", savedQuery.getId());

            return aiQueryMapper.toChatResponse(savedQuery);

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
                .map(aiQueryMapper::toHistoryResponse )
                .toList();
    }

}
