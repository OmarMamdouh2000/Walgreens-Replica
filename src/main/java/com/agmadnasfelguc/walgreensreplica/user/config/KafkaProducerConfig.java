package com.agmadnasfelguc.walgreensreplica.user.config;

import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.messaging.Message;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public Map<String,Object> producerConfig(){
        HashMap<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, Message<Object>> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<String, Message<Object>> kafkaTemplate(
            ProducerFactory<String, Message<Object>> producerFactory
    ) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ReplyingKafkaTemplate<String, Message<Object>, Message<Object>> replyKafkaTemplate(
            ProducerFactory<String, Message<Object>> producerFactory,
            KafkaMessageListenerContainer<String, Message<Object>> replyContainer
    ) {
        return new ReplyingKafkaTemplate<>(producerFactory, replyContainer);
    }

    @Bean
    public KafkaMessageListenerContainer<String, Message<Object>> replyContainer(ConsumerFactory<String, Object> cf) {
        ContainerProperties containerProperties = new ContainerProperties("responseTopic");
        containerProperties.setGroupId("user");
        return new KafkaMessageListenerContainer<>(cf, containerProperties);
    }


}
