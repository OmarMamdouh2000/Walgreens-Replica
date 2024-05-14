package com.example.Commands;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
        String secretKey = "ziad1234aaaa&&&&&thisisasecretekeyaaa"; 
        String token=(String)data.get("token");
        String transactionNumber=(String)data.get("transactionNumber");
        Claims claims = jwtDecoderService.decodeJwtToken(token, secretKey);
        if (claims != null) {
            String user = (String)claims.get("userId");
            CartTable userCart = cartRepo.getCart(UUID.fromString(user));
            // call createOrderAPi or publish to kafka orders with items and transaction
            userCart.getItems().clear();
            userCart.setTotalAmount(0);
            userCart.setAppliedPromoCodeId(null);
            userCart.setPromoCodeAmount(0);
            cartRepo.save(userCart);

            return "Order Placed Successfully";
        } 
        else{
            return "Invalid Token";
        }
        
    }


}
