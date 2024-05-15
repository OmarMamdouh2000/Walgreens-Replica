package com.example.Commands;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.common.Uuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Final.CartRepo;
import com.example.Final.CartTable;
import com.example.Final.PromoRepo;
import com.example.Final.UserUsedPromoRepo;
import com.example.Kafka.KafkaProducer;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
@Service
public class ConfirmCheckoutCommand implements Command {

    private JwtDecoderService jwtDecoderService;
    
    private CartRepo cartRepo;
    
    private KafkaProducer kafkaProducer;
    
    @Autowired
    public ConfirmCheckoutCommand(CartRepo cartRepo, JwtDecoderService jwtDecoderService, PromoRepo promoRepo, UserUsedPromoRepo userUsedPromoRepo,KafkaProducer kafkaProducer) {
    	this.cartRepo=cartRepo;
    	this.jwtDecoderService=jwtDecoderService;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public Object execute(Map<String, Object> data) throws Exception {
        data.put("commandName", "CreateOrder");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getMessage();
        }

        kafkaProducer.publishToTopic("orderRequests",jsonString);

        
        String user=(String)data.get("userId");

        String transactionNumber=(String)data.get("transactionNumber");
       
        CartTable userCart = cartRepo.getCart(UUID.fromString(user));
        // call createOrderAPi or publish to kafka orders with items and transaction
        userCart.getItems().clear();
        userCart.setTotalAmount(0);
        userCart.setAppliedPromoCodeId("");
        userCart.setPromoCodeAmount(0);
        cartRepo.save(userCart);

        return "Order Placed Successfully";
    
        
    }


}
