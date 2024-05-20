package com.walgreens.payment.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.util.*;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public Map<String, Object>  producerConfig(){
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory(){
        return new DefaultKafkaProducerFactory<String, String>(producerConfig());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ReplyingKafkaTemplate<String, String, String> replyKafkaTemplate(
            ProducerFactory<String, String> producerFactory,
            KafkaMessageListenerContainer<String, String> replyContainer
    ){
        return new ReplyingKafkaTemplate<>(producerFactory, replyContainer);
    }

    @Bean
    public KafkaMessageListenerContainer<String, String> replyContainer(
            ConsumerFactory<String, String> cf
    ){
        ContainerProperties containerProperties = new ContainerProperties("responseTopic");
        containerProperties.setGroupId("payment");
        return  new KafkaMessageListenerContainer<>(cf, containerProperties);
    }


}
