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
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;

@Service
public class ReturnFromSavedForLaterCommandCache implements Command {
    private JwtDecoderService jwtDecoderService;
    
    private CartRepo cartRepo;
    
    private KafkaProducer kafkaProducer;

    @Autowired  
	private SessionCache sessionCache;
    
    @Autowired
    public ReturnFromSavedForLaterCommandCache(CartRepo cartRepo, JwtDecoderService jwtDecoderService, PromoRepo promoRepo, UserUsedPromoRepo userUsedPromoRepo,KafkaProducer kafkaProducer, SessionCache sessionCache) {
    	this.cartRepo=cartRepo;
    	this.jwtDecoderService=jwtDecoderService;
        this.kafkaProducer = kafkaProducer;
        this.sessionCache = sessionCache;
    }

    @Override
    public Object execute(Map<String, Object> data) {
        String itemId=(String)data.get("itemId");
        String sessionId=(String)data.get("sessionId");
        Map<String, Object> session = sessionCache.getSessionSection(sessionId, "cart");
        String userId=(String) data.get("userId");
        if(userId==null)
            return "User not found or Invalid Token";
        CartTable oldCart=null;
        ObjectMapper objectMapper = new ObjectMapper();
        if(session!=null){
            oldCart = objectMapper.convertValue(session, CartTable.class);
        }else{
            oldCart=cartRepo.getCart(UUID.fromString(userId));
            sessionCache.createSession(userId, "cart", objectMapper.convertValue(oldCart, Map.class));
        }
        List<CartItem> oldItems=oldCart.getItems();
        List<CartItem> newSaved=oldCart.getSavedForLaterItems();
        UUID cartId=oldCart.getId();
        boolean found=false;
        double newTotal=0;
        if(oldItems==null) {
            oldItems=new ArrayList<CartItem>();
        }
        for(int i=0;newSaved!=null && i<newSaved.size();i++) {
            if(newSaved.get(i).getItemId().equals(UUID.fromString(itemId))) {
                oldItems.add(newSaved.get(i));
                int oldcount=newSaved.get(i).getItemCount();
                double increase=(oldcount)*newSaved.get(i).getPurchasedPrice();
                if(oldCart.getAppliedPromoCodeId() !=null) {
                    increase=increase - increase*oldCart.getPromoCodeAmount()/100.0;
                }
                newTotal=oldCart.getTotalAmount()+increase;
                newSaved.remove(i);
                found=true;
            }
        }
        if(found){
            oldCart.setTotalAmount(newTotal);
            sessionCache.updateSessionSection(sessionId, "cart", objectMapper.convertValue(oldCart, Map.class), 10,TimeUnit.HOURS);
        }else {
            return "invalid item id";
        }
        
        return "successfully returned from saved for later cache";

    }

}
