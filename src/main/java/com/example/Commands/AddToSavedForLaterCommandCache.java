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
public class AddToSavedForLaterCommandCache implements Command {
    @Autowired
    private JwtDecoderService jwtDecoderService;
    
    private CartRepo cartRepo;
    
    private KafkaProducer kafkaProducer;

    @Autowired
	private SessionCache sessionCache;
    
    @Autowired
    public AddToSavedForLaterCommandCache(CartRepo cartRepo, JwtDecoderService jwtDecoderService, PromoRepo promoRepo, UserUsedPromoRepo userUsedPromoRepo,KafkaProducer kafkaProducer, SessionCache sessionCache) {
    	this.cartRepo=cartRepo;
    	this.jwtDecoderService=jwtDecoderService;
        this.kafkaProducer = kafkaProducer;
        this.sessionCache = sessionCache;

    }

    @Override
    public Object execute(Map<String, Object> data) {
        String itemId=(String)data.get("itemId");
        String userId=(String)data.get("userId");
        String sessionId = (String)data.get("sessionId");
        Map<String, Object> session = sessionCache.getSessionSection(sessionId, "cart");
        ObjectMapper objectMapper = new ObjectMapper();
        CartTable oldCart = objectMapper.convertValue(session, CartTable.class);
        List<CartItem> oldItems=oldCart.getItems();
        List<CartItem> newSaved=oldCart.getSavedForLaterItems();
        if(newSaved==null) {
            newSaved=new ArrayList<CartItem>();
        }
        UUID cartId=oldCart.getId();
        boolean found=false;
        double newTotal=0;
        for(int i=0;i<oldItems.size();i++) {
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
        
        return "successfully added to saved for later";
        
    }


}
