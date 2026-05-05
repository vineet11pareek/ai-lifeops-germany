package com.lifeops.aiservice.service;

import com.lifeops.aiservice.dto.AiChatResponse;
import com.lifeops.aiservice.exception.AiProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AiChatService {

    private static Logger log = LoggerFactory.getLogger(AiChatService.class);

    private final ChatClient chatClient;
    private final String model;

    public AiChatService(ChatClient.Builder chatClientBuilder,
                         @Value("${spring.ai.openai.chat.options.model}") String model){
        this.chatClient = chatClientBuilder.build();
        this.model = model;
    }

    public AiChatResponse ask(String question){

        log.info("Processing AI chat request using model={}", model);

        try {
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
            log.info("AI chat request completed successfully");
            return new AiChatResponse(
                    answer,
                    "OPENAI",
                    model
            );
        } catch (Exception exception) {
            log.error("AI chat request failed", exception);
            throw new AiProcessingException(
                    "AI provider is temporarily unavailable. Please try again later.",
                    exception
            );
        }
    }
}
