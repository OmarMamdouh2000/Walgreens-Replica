package com.example.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.example.Cache.SessionCache;
import com.example.Final.*;
import com.example.Kafka.KafkaProducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;

@Service
public class ReturnFromSavedForLaterCommand implements Command {
    private JwtDecoderService jwtDecoderService;
    
    private CartRepo cartRepo;
    
    private KafkaProducer kafkaProducer;

    @Autowired  
	private SessionCache sessionCache;
    
    @Autowired
    public ReturnFromSavedForLaterCommand(CartRepo cartRepo, JwtDecoderService jwtDecoderService, PromoRepo promoRepo, UserUsedPromoRepo userUsedPromoRepo,KafkaProducer kafkaProducer, SessionCache sessionCache) {
    	this.cartRepo=cartRepo;
    	this.jwtDecoderService=jwtDecoderService;
        this.kafkaProducer = kafkaProducer;
        this.sessionCache = sessionCache;
    }

    @Override
    public Object execute(Map<String, Object> data) {
        String itemId=(String)data.get("itemId");
        String userId=(String)data.get("userId");
        CartTable oldCart=cartRepo.getCart(UUID.fromString(userId));
        List<CartItem> oldItems=oldCart.getItems();
        List<CartItem> newSaved=oldCart.getSavedForLaterItems();
        UUID cartId=oldCart.getId();
        boolean found=false;
        double newTotal=0;
        if(oldItems==null) {
            oldItems=new ArrayList<CartItem>();
        }
        for(int i=0;i<newSaved.size();i++) {
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
        if(found)
            cartRepo.updateCartItemsAndSaved(oldItems,newSaved, cartId,newTotal);
        else {
            return "invalid item id";
        }
        
        return "success";

    }

}
