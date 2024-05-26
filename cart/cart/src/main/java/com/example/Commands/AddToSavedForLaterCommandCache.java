package com.example.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.example.Cache.SessionCache;
import com.example.Final.*;
import com.example.Kafka.KafkaProducer;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;

@Service
public class AddToSavedForLaterCommandCache implements Command {
    @Autowired
    private JwtDecoderService jwtDecoderService;
    
    private CartRepo cartRepo;
    
    private KafkaProducer kafkaProducer;

    @Autowired
	private SessionCache sessionCache;
    private ReplyingKafkaTemplate<String, Message<String>, Message<String>> replyingKafkaTemplate;
    
    @Autowired
    public AddToSavedForLaterCommandCache(CartRepo cartRepo, JwtDecoderService jwtDecoderService, PromoRepo promoRepo, UserUsedPromoRepo userUsedPromoRepo,KafkaProducer kafkaProducer, SessionCache sessionCache,ReplyingKafkaTemplate<String, Message<String>, Message<String>> replyingKafkaTemplate) {
    	this.cartRepo=cartRepo;
    	this.jwtDecoderService=jwtDecoderService;
        this.kafkaProducer = kafkaProducer;
        this.sessionCache = sessionCache;

    }

    @Override
    public Object execute(Map<String, Object> data) {
        String itemId=(String)data.get("itemId");
        
        String sessionId = (String)data.get("sessionId");
        Map<String, Object> session = sessionCache.getSessionSection(sessionId, "cart");
        String userId=(String) data.get("userId");
        if(userId==null)
            return "User not found or Invalid Token";
        ObjectMapper objectMapper = new ObjectMapper();
        CartTable oldCart =null;
        if(session !=null && !session.isEmpty()) {
            oldCart = objectMapper.convertValue(session, CartTable.class);
        }else{
            oldCart=cartRepo.getCart(UUID.fromString(userId));
            sessionCache.createSession(sessionId, "cart", objectMapper.convertValue(oldCart, Map.class));
        }
        List<CartItem> oldItems=oldCart.getItems();
        List<CartItem> newSaved=oldCart.getSavedForLaterItems();
        if(newSaved==null) {
            newSaved=new ArrayList<CartItem>();
        }
        UUID cartId=oldCart.getId();
        boolean found=false;
        double newTotal=0;
        System.out.println(oldCart);
        System.out.println(oldItems);
        for(int i=0;oldItems !=null && i<oldItems.size();i++) {
            if(oldItems.get(i).getItemId().equals(UUID.fromString(itemId))) {
                newSaved.add(oldItems.get(i));
                int oldcount=oldItems.get(i).getItemCount();
                double increase=(0-oldcount)*oldItems.get(i).getPurchasedPrice();
                if(oldCart.getAppliedPromoCodeId() !=null) {
                    increase=increase - increase*oldCart.getPromoCodeAmount()/100.0;
                }
                newTotal=oldCart.getTotalAmount()+increase;
                oldItems.remove(i);
                found=true;
            }
        }
        if(found){
            oldCart.setTotalAmount(newTotal);
            Map<String, Object> newData = objectMapper.convertValue(oldCart, Map.class);
            sessionCache.updateSessionSection(sessionId, "cart", newData, 10, TimeUnit.HOURS);


        }
        else {
            return "invalid item id";
        }
        
        return "successfully added to saved for later cache";
        
    }


}
