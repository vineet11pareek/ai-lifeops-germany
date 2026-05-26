package com.lifeops.truthservice.service;

import com.lifeops.truthservice.event.TruthAnalyzedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TruthEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(TruthEventPublisher.class);

    private final KafkaTemplate<String, TruthAnalyzedEvent> kafkaTemplate;
    private final String truthAnalyzedTopic;

    public TruthEventPublisher(
            KafkaTemplate<String, TruthAnalyzedEvent> kafkaTemplate,
            @Value("${lifeops.kafka.topics.truth-analyzed}") String truthAnalyzedTopic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.truthAnalyzedTopic = truthAnalyzedTopic;
    }

    public void publish(TruthAnalyzedEvent event) {
        String key = event.analysisId().toString();

        kafkaTemplate.send(truthAnalyzedTopic, key, event);

        log.info(
                "Published truth analyzed event eventId={}, analysisId={}, topic={}",
                event.eventId(),
                event.analysisId(),
                truthAnalyzedTopic
        );
    }
}
