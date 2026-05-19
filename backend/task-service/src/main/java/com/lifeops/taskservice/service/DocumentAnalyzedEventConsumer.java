package com.lifeops.taskservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifeops.taskservice.event.DocumentAnalyzedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DocumentAnalyzedEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(DocumentAnalyzedEventConsumer.class);
    private final ObjectMapper objectMapper;
    private final TaskService taskService;

    public DocumentAnalyzedEventConsumer(ObjectMapper objectMapper, TaskService taskService) {
        this.objectMapper = objectMapper;
        this.taskService = taskService;
    }

    @KafkaListener(
            topics = "${lifeops.kafka.topics.document-analyzed}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(String rawEvent){
        try {
            DocumentAnalyzedEvent event = objectMapper.readValue(rawEvent, DocumentAnalyzedEvent.class);
            log.info(
                    "Received document analyzed event eventId={}, documentId={}, title={}, riskLevel={}, status={}",
                    event.eventId(),
                    event.documentId(),
                    event.title(),
                    event.riskLevel(),
                    event.status()
            );
           taskService.createTaskFromDocumentAnalyzedEvent(event);
        } catch (JsonProcessingException e) {
            //TODO: create a custom exception and handle from global exception, instead of only log and ignore
            log.error("Deserialisation of an event having issue",e);
        }

    }
}
