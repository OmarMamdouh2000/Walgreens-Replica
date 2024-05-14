package com.example.Commands;

import com.example.Final.*;
import com.example.Kafka.KafkaProducer;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public GetUserCart(CartRepo cartRepo, JwtDecoderService jwtDecoderService, PromoRepo promoRepo, UserUsedPromoRepo userUsedPromoRepo,KafkaProducer kafkaProducer) {
    	this.cartRepo=cartRepo;
    	this.jwtDecoderService=jwtDecoderService;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public Object execute(Map<String,Object> data) throws Exception {
        String token = (String)data.get("token");
        Claims claims = jwtDecoderService.decodeJwtToken(token, "ziad1234aaaa&&&&&thisisasecretekeyaaa");

        if(claims == null) return "Invalid Token";

        String user = (String)claims.get("userId");
        UUID userId = UUID.fromString(user);
        try{
            CartTable userCart = cartRepo.getCart(userId);
            if(userCart == null){
                userCart = cartRepo.createNewCart(userId);
            }
            return userCart;
        }catch (Exception e){
            return e.getMessage();
        }
    }
}
