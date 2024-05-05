package com.agmadnasfelguc.walgreensreplica.user.service.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.stereotype.Service;

@Service
public class KafkaListeners {

    @KafkaListener(topics = "userManagement", groupId = "user")
    void listener(String message) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            JsonNode rootNode = mapper.readTree(message);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }

}
