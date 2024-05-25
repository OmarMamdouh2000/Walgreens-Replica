package com.example.Commands;

import com.example.Cache.SessionCache;
import com.example.Final.*;
import com.example.Kafka.KafkaProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@Service
public class GetUserCart implements Command {
    @Autowired
    private JwtDecoderService jwtDecoderService;

    private CartRepo cartRepo;

    private KafkaProducer kafkaProducer;

    @Autowired
	private SessionCache sessionCache;

    private ReplyingKafkaTemplate<String, Message<String>, Message<String>> replyingKafkaTemplate;
    
    @Autowired
    public GetUserCart(CartRepo cartRepo, JwtDecoderService jwtDecoderService, PromoRepo promoRepo, UserUsedPromoRepo userUsedPromoRepo,KafkaProducer kafkaProducer, SessionCache sessionCache,ReplyingKafkaTemplate<String, Message<String>, Message<String>> replyingKafkaTemplate) {
    	this.cartRepo=cartRepo;
    	this.jwtDecoderService=jwtDecoderService;
        this.kafkaProducer = kafkaProducer;
        this.sessionCache = sessionCache;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object execute(Map<String,Object> data) throws Exception {
        String sessionId = (String) data.get("sessionId");

        String user = (String)data.get("userId");
        if(user==null)
            return "User not found or Invalid Token";
        UUID userId = UUID.fromString(user);

        ObjectMapper objectMapper = new ObjectMapper();

        CartTable userCart;
        System.out.println("Before Cache");
        Map<String, Object> cachedCart = sessionCache.getSessionSection(sessionId, "cart");
        System.out.println("After Cache");
        if(!cachedCart.isEmpty()){
            System.out.println("Cart from Cache");
            userCart = objectMapper.convertValue(cachedCart, CartTable.class);
            return userCart;
        }

        try{
            userCart = cartRepo.getCart(userId);
            if(userCart == null){
                userCart = cartRepo.createNewCart(userId);
            }

            String jsonString = null;
            jsonString = objectMapper.writeValueAsString(userCart);

            Map<String, Object> map = objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {});
            System.out.println(map);
            sessionCache.createSession((String)data.get("sessionId"), "cart", map);

            return userCart;

        }catch (Exception e){
            return e.getMessage();
        }
    }
}
