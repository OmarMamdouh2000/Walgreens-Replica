package com.example.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.example.Final.*;
import com.example.Kafka.KafkaProducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;

@Service
public class AddToSavedForLaterCommand implements Command {
    @Autowired
    private JwtDecoderService jwtDecoderService;
    
    private CartRepo cartRepo;
    
    private KafkaProducer kafkaProducer;
    
    @Autowired
    public AddToSavedForLaterCommand(CartRepo cartRepo, JwtDecoderService jwtDecoderService, PromoRepo promoRepo, UserUsedPromoRepo userUsedPromoRepo,KafkaProducer kafkaProducer) {
    	this.cartRepo=cartRepo;
    	this.jwtDecoderService=jwtDecoderService;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public Object execute(Map<String, Object> data) {
        String itemId=(String)data.get("itemId");
        String token=(String)data.get("token");


        String secretKey = "ziad1234aaaa&&&&&thisisasecretekeyaaa"; 
	    Claims claims = jwtDecoderService.decodeJwtToken(token, secretKey);
        if (claims != null) {
            // Extract claims from the JWT token and perform necessary actions
            String userId=(String) claims.get("userId");
            CartTable oldCart=cartRepo.getCart(UUID.fromString(userId));
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
            if(found)
            	cartRepo.updateCartItemsAndSaved(oldItems,newSaved, cartId,newTotal);
            else {
            	return "invalid item id";
            }
            
            return "success";
            
        } else {
            return "failed";
        }
        
    }


}
