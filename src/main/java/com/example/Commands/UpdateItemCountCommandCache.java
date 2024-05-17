package com.example.Commands;

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
public class UpdateItemCountCommandCache implements Command{
    
    private JwtDecoderService jwtDecoderService;
    
    private CartRepo cartRepo;
    
    private KafkaProducer kafkaProducer;

    @Autowired
	private SessionCache sessionCache;
    
    @Autowired
    public UpdateItemCountCommandCache(CartRepo cartRepo, JwtDecoderService jwtDecoderService, PromoRepo promoRepo, UserUsedPromoRepo userUsedPromoRepo,KafkaProducer kafkaProducer, SessionCache sessionCache) {
    	this.cartRepo=cartRepo;
    	this.jwtDecoderService=jwtDecoderService;
        this.kafkaProducer = kafkaProducer;
        this.sessionCache = sessionCache;
    }
    
    
    @Override
    public Object execute(Map<String,Object> data) {
        
        String itemId=(String)data.get("itemId");
        String sessionId=(String)data.get("sessionId");
        Map<String, Object> sessionCart = sessionCache.getSessionSection(sessionId, "cart");
        Map<String, Object> sessionUser = sessionCache.getSessionSection(sessionId, "user");
        ObjectMapper objectMapper = new ObjectMapper();
        int count=(int)data.get("itemCount");
	   
        if(count<0) {
            return "invalid count";
        }
        String userId=(String) sessionUser.get("userId");
        CartTable oldCart=null;
        if(sessionCart !=null) {
            oldCart = objectMapper.convertValue(sessionCart, CartTable.class);
        }else{
            oldCart=cartRepo.getCart(UUID.fromString(userId));
            sessionCache.createSession(sessionId, "cart", objectMapper.convertValue(oldCart, Map.class));
        }
        List<CartItem> oldItems=oldCart.getItems();
        UUID cartId=oldCart.getId();
        boolean found=false;
        double newTotal=0;
        for(int i=0;oldItems!=null && i<oldItems.size();i++) {
            if(oldItems.get(i).getItemId().equals(UUID.fromString(itemId))) {
                if(count>0) {
                    int oldcount=oldItems.get(i).getItemCount();
                    oldItems.get(i).setItemCount(count);
                    double increase=(count-oldcount)*oldItems.get(i).getPurchasedPrice();
                    if(oldCart.getAppliedPromoCodeId() !=null) {
                        increase=increase - increase*oldCart.getPromoCodeAmount()/100.0;
                    }
                    newTotal=oldCart.getTotalAmount()+increase;
                    System.out.println(newTotal);
                }else {
                    int oldcount=oldItems.get(i).getItemCount();
                    double increase=(count-oldcount)*oldItems.get(i).getPurchasedPrice();
                    if(oldCart.getAppliedPromoCodeId() !=null) {
                        increase=increase - increase*oldCart.getPromoCodeAmount()/100.0;
                    }
                    newTotal=oldCart.getTotalAmount()+increase;
                    
                    oldItems.remove(i);
                    
                    
                }
                found=true;
                break;
            }
        }
        if(found){
            oldCart.setTotalAmount(newTotal);
            sessionCache.updateSessionSection(sessionId, "cart", objectMapper.convertValue(oldCart, Map.class), 10,TimeUnit.HOURS);
            
        }else {
            return "invalid item id";
        }
        
        return "successfully updated item count cache";

    }

}
