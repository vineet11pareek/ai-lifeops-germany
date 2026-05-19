package com.lifeops.taskservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifeops.taskservice.event.DocumentEventAnalyzed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DocumentAnalyzedEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(DocumentAnalyzedEventConsumer.class);
    private final ObjectMapper objectMapper;

    public DocumentAnalyzedEventConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "${lifeops.kafka.topics.document-analyzed}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(String rawEvent){
        try {
            DocumentEventAnalyzed event = objectMapper.readValue(rawEvent, DocumentEventAnalyzed.class);
            log.info(
                    "Received document analyzed event eventId={}, documentId={}, title={}, riskLevel={}, status={}",
                    event.eventId(),
                    event.documentId(),
                    event.title(),
                    event.riskLevel(),
                    event.status()
            );
        } catch (JsonProcessingException e) {
            log.error("Deserialisation of an event having issue");
        }

    }
}
