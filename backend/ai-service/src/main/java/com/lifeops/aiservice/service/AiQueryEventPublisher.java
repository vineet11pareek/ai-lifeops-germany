package com.lifeops.aiservice.service;

import com.lifeops.aiservice.event.AiQueryCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AiQueryEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(AiQueryEventPublisher.class);

    private final KafkaTemplate<String, AiQueryCompletedEvent> kafkaTemplate;
    private final String aiQueryCompletedTopic;

    public AiQueryEventPublisher(KafkaTemplate<String, AiQueryCompletedEvent> kafkaTemplate,
                                 @Value("${lifeops.kafka.topics.ai-query-completed}") String aiQueryCompletedTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.aiQueryCompletedTopic = aiQueryCompletedTopic;
    }

    public void publish(AiQueryCompletedEvent event){

        String key = event.queryId().toString();

        kafkaTemplate.send(aiQueryCompletedTopic,key,event);
       /* kafkaTemplate.send("ai.query.completed",key,event)
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        log.error("Kafka publish failed topic={}", "ai.query.completed", exception);
                    } else {
                        log.info("Kafka publish success topic={}, partition={}, offset={}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });*/

        log.info("Published AI query completed event eventId={}, queryId={}, topic={}",
                event.eventId(),
                event.queryId(),
                aiQueryCompletedTopic);
    }
}
