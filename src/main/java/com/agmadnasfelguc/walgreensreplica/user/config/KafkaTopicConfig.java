package com.agmadnasfelguc.walgreensreplica.user.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic userManagementTopic(){
        return TopicBuilder.name("userManagement")
                .partitions(3)
                .build();
    }

    @Bean
    public NewTopic controllerTopic(){
        return TopicBuilder.name("controller")
                .partitions(1)
                .build();
    }
}
