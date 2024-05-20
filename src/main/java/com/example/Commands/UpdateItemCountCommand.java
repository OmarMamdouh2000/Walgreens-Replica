package com.example.Commands;

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
public class UpdateItemCountCommand implements Command{
    
    private JwtDecoderService jwtDecoderService;
    
    private CartRepo cartRepo;
    
    private KafkaProducer kafkaProducer;

    @Autowired
	private SessionCache sessionCache;
    
    @Autowired
    public UpdateItemCountCommand(CartRepo cartRepo, JwtDecoderService jwtDecoderService, PromoRepo promoRepo, UserUsedPromoRepo userUsedPromoRepo,KafkaProducer kafkaProducer, SessionCache sessionCache) {
    	this.cartRepo=cartRepo;
    	this.jwtDecoderService=jwtDecoderService;
        this.kafkaProducer = kafkaProducer;
        this.sessionCache = sessionCache;
    }
    
    
    @Override
    public Object execute(Map<String,Object> data) {
        
        String itemId=(String)data.get("itemId");
        int count=(int)data.get("itemCount");
	   
        if(count<0) {
            return "invalid count";
        }
        String userId=(String) data.get("userId");
        CartTable oldCart=cartRepo.getCart(UUID.fromString(userId));
        List<CartItem> oldItems=oldCart.getItems();
        UUID cartId=oldCart.getId();
        boolean found=false;
        double newTotal=0;
        for(int i=0;i<oldItems.size();i++) {
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
        if(found)
            cartRepo.updateCartItems(oldItems, cartId,newTotal);
        else {
            return "invalid item id";
        }
        
        return "successfully updated item count in DB";

    }

}
