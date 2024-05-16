package com.agmadnasfelguc.walgreensreplica.user.service.kafka.message;

import com.agmadnasfelguc.walgreensreplica.user.service.kafka.KafkaPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserRequests {

    @Autowired
    private KafkaPublisher publisher;

//    public void sendRequest(Map<String,String> paramsMap, Map<String,String> bodyMap) {
//
//
//        publisher.publish("userManagement", message);
//    }



}
