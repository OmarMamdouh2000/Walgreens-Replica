package com.example.Commands;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Final.CartItem;
import com.example.Final.CartRepo;
import com.example.Final.CartTable;

import io.jsonwebtoken.Claims;

@Service
public class UpdateItemCountCommand implements Command{
    
    private JwtDecoderService jwtDecoderService;
    
    private CartRepo cartRepo;
    
    @Autowired
    public UpdateItemCountCommand(CartRepo cartRepo,JwtDecoderService jwtDecoderService) {
    	this.cartRepo=cartRepo;
    	this.jwtDecoderService=jwtDecoderService;
    }
    
    
    @Override
    public Object execute(Map<String,Object> data) {
        String secretKey = "ziad1234aaaa&&&&&thisisasecretekeyaaa"; 
        
        String itemId=(String)data.get("itemId");
        String token=(String)data.get("token");
        int count=(int)data.get("itemCount");
	    
        Claims claims = jwtDecoderService.decodeJwtToken(token, secretKey);
        if (claims != null) {
            // Extract claims from the JWT token and perform necessary actions
            if(count<0) {
            	return "invalid count";
            }
            String userId=(String) claims.get("userId");
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
            
            return "success";
            
        } else {
            return "failed";
        }
    }

}
