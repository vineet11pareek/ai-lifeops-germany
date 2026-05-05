package com.lifeops.aiservice.service;

import com.lifeops.aiservice.dto.AiChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AiChatService {

    private final ChatClient chatClient;
    private final String model;

    public AiChatService(ChatClient.Builder chatClientBuilder,
                         @Value("${spring.ai.openai.chat.options.model}") String model){
        this.chatClient = chatClientBuilder.build();
        this.model = model;
    }

    public AiChatResponse ask(String question){
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

        return new AiChatResponse(
                answer,
                "OPENAI",
                model
        );
    }
}
