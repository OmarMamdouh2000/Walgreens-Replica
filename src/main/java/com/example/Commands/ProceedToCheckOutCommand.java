package com.example.Commands;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.common.Uuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Cache.SessionCache;
import com.example.Final.CartRepo;
import com.example.Final.CartTable;
import com.example.Final.PromoRepo;
import com.example.Final.UserUsedPromoRepo;
import com.example.Kafka.KafkaProducer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class ProceedToCheckOutCommand implements Command {

    private JwtDecoderService jwtDecoderService;
    
    private CartRepo cartRepo;
    
    private KafkaProducer kafkaProducer;

    @Autowired
	private SessionCache sessionCache;
    
    @Autowired
    public ProceedToCheckOutCommand(CartRepo cartRepo, JwtDecoderService jwtDecoderService, PromoRepo promoRepo, UserUsedPromoRepo userUsedPromoRepo,KafkaProducer kafkaProducer, SessionCache sessionCache) {
    	this.cartRepo=cartRepo;
    	this.jwtDecoderService=jwtDecoderService;
        this.kafkaProducer = kafkaProducer;
        this.sessionCache = sessionCache;
    }

    @Override
    public Object execute(Map<String, Object> data) throws Exception {
        Map<String,Object> session = sessionCache.getSessionSection((String)data.get("sessionId"), "cart");
        String sessionId=(String)data.get("sessionId");
        String user = (String)data.get("userId");
        if(user==null)
            return "User not found or Invalid Token";
        CartTable userCart = null;
        ObjectMapper objectMapper = new ObjectMapper();
        
        if(session!=null &&!session.isEmpty()){
            
            userCart = objectMapper.convertValue(session, CartTable.class);
        }else{  
            userCart = cartRepo.getCart(UUID.fromString(user));
            sessionCache.createSession(sessionId, "cart", objectMapper.convertValue(userCart, Map.class));
        }
        System.out.println(userCart);
        Map<String, Object> request = new HashMap<>();
        
        
        //------------ karim integration
        // request.put("request", "CheckPaymentMethod");
        // request.put("customerUuid", user);
        // request.put("paymentAmount", userCart.getTotalAmount()+"");
        // request.put("cartUuid", userCart.getId().toString());
        // request.put("cartItems", userCart.getItems());
        // request.putAll(data);
        // JsonNode jsonNode = objectMapper.convertValue(request, JsonNode.class);
        // String userCartString = objectMapper.writeValueAsString(jsonNode);
        // UUID corrId=UUID.randomUUID();
        // byte[] corrIdBytes = corrId.toString().getBytes();
        // // userCartString=userCartString.replace("\\", "");
        // // userCartString=userCartString.substring(1, userCartString.length()-1);
        
        // kafkaProducer.publishToTopic("payment",userCartString, corrIdBytes);
        
        // return "Proceed to checkout request sent";


        //---------- no integration
        request.putAll(data);
        request.put("commandName", "ConfirmCheckoutCommand");
        request.put("transactionNumber",UUID.randomUUID().toString());
        String userCartString = objectMapper.writeValueAsString(request);
        UUID corrId=UUID.randomUUID();
        byte[] corrIdBytes = corrId.toString().getBytes();
        kafkaProducer.publishToTopic("cartRequests", userCartString,corrIdBytes);
        return "Proceed to checkout request sent";

        
    }


}
