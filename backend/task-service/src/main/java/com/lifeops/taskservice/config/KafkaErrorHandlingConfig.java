package com.lifeops.taskservice.config;

import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaErrorHandlingConfig {

    @Bean
    public DefaultErrorHandler defaultErrorHandler(KafkaOperations<String,Object> kafkaOperations,
                                                   @Value("${lifeops.kafka.topics.document-analyzed-dlq}") String documentAnalyzedDlqTopic){
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
                kafkaOperations,
                (consumerRecord,exception)-> new TopicPartition(
                        documentAnalyzedDlqTopic,consumerRecord.partition()
                )
        );

        FixedBackOff backOff = new FixedBackOff(
                1000l,
                3L
        );

        return new DefaultErrorHandler(recoverer,backOff);
    }
}
