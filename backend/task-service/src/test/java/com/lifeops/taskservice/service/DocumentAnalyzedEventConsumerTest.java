package com.lifeops.taskservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lifeops.taskservice.event.DocumentAnalyzedEvent;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


class DocumentAnalyzedEventConsumerTest {

    private  ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldDelegateEventToTaskService() throws JsonProcessingException {
        objectMapper.registerModule(new JavaTimeModule());
        TaskService taskService = mock(TaskService.class);
        DocumentAnalyzedEventConsumer consumer = new DocumentAnalyzedEventConsumer(objectMapper,taskService);

        DocumentAnalyzedEvent event = new DocumentAnalyzedEvent(
                UUID.randomUUID(),
                UUID.randomUUID(),
                null,
                "Letter from Finanzamt",
                "Summary",
                "15.06.2026",
                "Submit documents",
                "MEDIUM",
                "ANALYZED",
                Instant.now()
        );

        consumer.consume(objectMapper.writeValueAsString(event));

        verify(taskService).createTaskFromDocumentAnalyzedEvent(event);
    }


}