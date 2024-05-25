package com.example.Commands;

import com.example.Cache.SessionCache;
import com.example.Final.*;
import com.example.Kafka.KafkaProducer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ChangeOrderType implements Command{
    @Autowired
    private JwtDecoderService jwtDecoderService;

    private CartRepo cartRepo;

    private KafkaProducer kafkaProducer;

    private ReplyingKafkaTemplate<String, Message<String>, Message<String>> replyingKafkaTemplate;

    @Autowired
	private SessionCache sessionCache;
    
    @Autowired
    public ChangeOrderType(CartRepo cartRepo, JwtDecoderService jwtDecoderService, PromoRepo promoRepo, UserUsedPromoRepo userUsedPromoRepo,KafkaProducer kafkaProducer, SessionCache sessionCache,ReplyingKafkaTemplate<String, Message<String>, Message<String>> replyingKafkaTemplate) {
    	this.cartRepo=cartRepo;
    	this.jwtDecoderService=jwtDecoderService;
        this.kafkaProducer = kafkaProducer;
        this.sessionCache = sessionCache;
    }

    @Override
    public Object execute(Map<String,Object> data) throws Exception {
        String sessionId = (String) data.get("sessionId");

        String user = (String)data.get("userId");
        if(user==null)
            return "User not found or Invalid Token";
        UUID userID = UUID.fromString(user);

        String itemString = (String) data.get("itemId");
        UUID itemID = UUID.fromString(itemString);

        String orderType = (String) data.get("orderType");

        CartTable userCart;
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> cachedCart = sessionCache.getSessionSection(sessionId, "cart");

        if(!cachedCart.isEmpty()) userCart = objectMapper.convertValue(cachedCart, CartTable.class);
        else userCart = cartRepo.getCart(userID);

        if(userCart != null){
            if(orderType.equals("shipping")){
                CartItem cartItem = userCart.getItems().stream()
                        .filter(item -> item.getItemId().equals(itemID)).
                        toList().get(0);
                cartItem.setDeliveryType("shipping");
            }else{
                userCart.getItems().forEach(item -> item.setDeliveryType(orderType));
            }

            String jsonString = objectMapper.writeValueAsString(userCart);
            Map<String, Object> map = objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {});
            sessionCache.updateSessionSection(sessionId, "cart", map, 10, TimeUnit.HOURS);

            return userCart;
        }else{
            return "Cart not found";
        }

    }
}
