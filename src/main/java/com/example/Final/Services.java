package com.example.Final;

import java.util.UUID;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import com.example.Commands.JwtDecoderService;

// @Service
// class JwtDecoderService {

//    public Claims decodeJwtToken(String token, String secretKey) {
//        try {
//            Jws<Claims> jws = Jwts.parserBuilder()
//                    .setSigningKey(secretKey.getBytes())
//                    .build()
//                    .parseClaimsJws(token);
//            return jws.getBody();
//        } catch (Exception e) {
//            // Handle exception (e.g., invalid token)
//            e.printStackTrace();
//            return null;
//        }
//    }
// }
@Service
public class Services {
	@Autowired
    private JwtDecoderService jwtDecoderService;
	@Autowired
	private CartRepo cartRepo;

    @Autowired
    private UserUsedPromoRepo userUsedPromoRepo;

    public CartTable getUserCart(String user) throws Exception {
        UUID userId = UUID.fromString(user);
        try{
            CartTable userCart = cartRepo.getCart(userId);
            if(userCart != null){
                return userCart;
            }else{
                return new CartTable(UUID.randomUUID(),"",new ArrayList<CartItem>(), "online", new ArrayList<CartItem>(), 0.0, userId);
            }
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
	
	
	

    public CartTable removeItemFromCart(String user,UUID itemId) throws Exception {
        UUID userId = UUID.fromString(user);
        try{
            CartTable userCart = cartRepo.getCart(userId);

            if(userCart != null){
                List<CartItem> updatedItems = userCart.getItems().stream()
                        .filter(item -> !item.getItemId().equals(itemId))
                        .collect(Collectors.toList());

                userCart.setItems(updatedItems);
                return cartRepo.save(userCart);
            }else{
                throw new Exception("Cart not found");
            }
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }

    public void setOrderType(String user, String orderType, String stringItem) throws Exception {
        UUID userID = UUID.fromString(user);
        UUID itemID = UUID.fromString(stringItem);
        try{
            CartTable userCart = cartRepo.getCart(userID);

            if(userCart != null){
                if(orderType.equals("shipping")){
                    CartItem cartItem = userCart.getItems().stream()
                            .filter(item -> item.getItemId().equals(itemID)).
                            toList().get(0);
                    cartItem.setDeliveryType("shipping");
                    cartRepo.save(userCart);
                }else{
                    userCart.getItems().forEach(item -> item.setDeliveryType(orderType));
                    cartRepo.save(userCart);
                }
            }else{
                throw new Exception("Cart not found");
            }
        }catch (Exception e){
            throw new Exception("Server error try again");
        }
    }

    public CartTable applyPromo(String user, String promoId){
        UUID userID = UUID.fromString(user);
        UUID promoID = UUID.fromString(promoId);
        System.out.println(userID);
        System.out.println(promoID);
        try{
            CartTable userCart = cartRepo.getCart(userID);

            if(userCart == null){
                throw new Exception("Cart not found");
            }

            //check if promo is in userUsedPromos --> if so return invalid
            UserUsedPromo isUsed = userUsedPromoRepo.findUserPromo(userID, promoID);
            System.out.println(isUsed);
            if(isUsed == null){
                //get promo from userPromos --> if not found return invalid
                PromoCodeTable promo = cartRepo.getPromoCode(promoID);

                if(promo == null){
                    throw new Exception("Promo not found");
                }
                else{
                    double totalAmount = userCart.getTotalAmount();
                    double promoAmount = promo.getDiscountValue();

                    //apply promo amount to total amount
                    userCart.setTotalAmount(totalAmount * (1 - promoAmount));

                    //add promo to userUsedPromos
//                userUsedPromoRepo.save(new UserUsedPromo(userID, promoID));
                    return cartRepo.save(userCart);
                }

            }else{
                throw new Exception("Promo already used");
            }

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
    public List<UserUsedPromo> getAllUsedPromo(){
		return userUsedPromoRepo.getAll();
	}
}
