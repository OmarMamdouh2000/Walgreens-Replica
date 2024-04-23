package com.example.Final;

import java.util.UUID;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

@Service
 class JwtDecoderService {

    public Claims decodeJwtToken(String token, String secretKey) {
        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token);
            return jws.getBody();
        } catch (Exception e) {
            // Handle exception (e.g., invalid token)
            e.printStackTrace();
            return null;
        }
    }
}
@Service
public class Services {
	@Autowired
    private JwtDecoderService jwtDecoderService;
	@Autowired
	private CartRepo cartRepo;
	@Autowired
	private UserUsedPromoRepo userUsedPromoRepo;
	public String updateItemCount(String itemId,String token,int count) {
		 
	    String secretKey = "ziad1234aaaa&&&&&thisisasecretekeyaaa"; 
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
	public String addToSavedForLater(String itemId,String token) {
		 
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
	public String returnFromSavedForLater(String itemId,String token) {
		 
	    String secretKey = "ziad1234aaaa&&&&&thisisasecretekeyaaa"; 
	    Claims claims = jwtDecoderService.decodeJwtToken(token, secretKey);
        if (claims != null) {
            // Extract claims from the JWT token and perform necessary actions
            String userId=(String) claims.get("userId");
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
            
        } else {
            return "failed";
        }

	}
	public List<UserUsedPromo> getAllUsedPromo(){
		return userUsedPromoRepo.getAll();
	}
}
