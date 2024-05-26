package com.example.Commands;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.common.Uuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyMessageFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.example.Cache.SessionCache;
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
	private SessionCache sessionCache;
    private ReplyingKafkaTemplate<String, Message<String>, Message<String>> replyingKafkaTemplate;
    
    @Autowired
    public ConfirmCheckoutCommand(CartRepo cartRepo, JwtDecoderService jwtDecoderService, PromoRepo promoRepo, UserUsedPromoRepo userUsedPromoRepo,KafkaProducer kafkaProducer, SessionCache sessionCache,ReplyingKafkaTemplate<String, Message<String>, Message<String>> replyingKafkaTemplate) {
    	this.cartRepo=cartRepo;
    	this.jwtDecoderService=jwtDecoderService;
        this.kafkaProducer = kafkaProducer;
        this.sessionCache = sessionCache;
        this.replyingKafkaTemplate=replyingKafkaTemplate;
    }

    @Override
    public Object execute(Map<String, Object> data) throws Exception {

        String user=(String)data.get("userId");
        if(user==null)
            return "User not found or Invalid Token";
        String sessionId=(String)data.get("sessionId");
        ObjectMapper objectMapper = new ObjectMapper();
        CartTable userCart = cartRepo.getCart(UUID.fromString(user));
        Map<String,Object> session = sessionCache.getSessionSection(sessionId, "cart");
        

        data.put("commandName", "CreateOrder");
        data.put("cart", userCart);

        
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        String random=UUID.randomUUID().toString();
        byte[] correlationId = random.toString().getBytes();

        //kafkaProducer.publishToTopic("orderRequests",jsonString,correlationId);
        RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
                    .withPayload(jsonString)
                    .setHeader(KafkaHeaders.REPLY_TOPIC, "cartResponses")
                    .setHeader(KafkaHeaders.TOPIC, "orderRequests")
                    .setHeader(KafkaHeaders.KEY, random)
					//.setHeader(KafkaHeaders.CORRELATION_ID, random)
                    .build());
        userCart.getItems().clear();
        userCart.setTotalAmount(0);
        userCart.setAppliedPromoCodeId("");
        userCart.setPromoCodeAmount(0);
        if(session!=null && !session.isEmpty()){
            CartTable userCartCache = objectMapper.convertValue(session, CartTable.class);
            userCartCache.getItems().clear();
            userCartCache.setTotalAmount(0);
            userCartCache.setAppliedPromoCodeId("");
            userCartCache.setPromoCodeAmount(0);
            sessionCache.updateSessionSection(sessionId, "cart", objectMapper.convertValue(userCartCache, Map.class),10,TimeUnit.HOURS);
        }
        cartRepo.save(userCart);

        return "Order Issue is Sent Successfully";
    
        
    }


}
