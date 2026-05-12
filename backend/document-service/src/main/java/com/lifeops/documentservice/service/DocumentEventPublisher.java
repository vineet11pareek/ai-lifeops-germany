package com.lifeops.documentservice.service;

import com.lifeops.documentservice.event.DocumentAnalyzedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DocumentEventPublisher {

    private final static Logger log = LoggerFactory.getLogger(DocumentEventPublisher.class);

    private final KafkaTemplate<String, DocumentAnalyzedEvent> kafkaTemplate;
    private final String documentAnalyzedTopic;

    public DocumentEventPublisher(KafkaTemplate<String, DocumentAnalyzedEvent> kafkaTemplate,
                                  @Value("${lifeops.kafka.topics.document-analyzed}") String documentAnalyzedTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.documentAnalyzedTopic = documentAnalyzedTopic;
    }

    public void publish(DocumentAnalyzedEvent event){

        String key = event.documentId().toString();
        kafkaTemplate.send(documentAnalyzedTopic,key,event);
        log.info(
                "Published document analyzed event eventId: {}, documentId: {}, topic: {}",
                event.eventId(),
                event.documentId(),
                documentAnalyzedTopic
        );
    }
}
