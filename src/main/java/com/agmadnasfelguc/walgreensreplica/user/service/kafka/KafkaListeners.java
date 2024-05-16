package com.agmadnasfelguc.walgreensreplica.user.service.kafka;

import com.agmadnasfelguc.walgreensreplica.user.service.invoker.UserInvoker;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.stereotype.Service;



@Service
public class KafkaListeners {
    @Autowired
    private UserInvoker userInvoker;
    Logger logger = LoggerFactory.getLogger(KafkaListeners.class);

    @KafkaListener(topics = "userManagement", groupId = "user")
    void listener(String message) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            JsonNode rootNode = mapper.readTree(message);
            System.out.println("Received message");
            System.out.println(rootNode);
            logger.info("Received message" + rootNode);
            userInvoker.callCommand(rootNode);
        }catch (Exception e){
            System.out.println(e.getMessage());
            logger.error(e.getMessage());
        }


    }

}
