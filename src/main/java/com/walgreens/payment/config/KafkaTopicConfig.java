package com.walgreens.payment.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

public class KafkaTopicConfig {


    @Bean
    public NewTopic paymentTopic(){
        return TopicBuilder
                .name("payment")
                .build();
    }
}
