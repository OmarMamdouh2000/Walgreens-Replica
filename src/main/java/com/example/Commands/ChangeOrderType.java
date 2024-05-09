package com.example.Commands;

import com.example.Final.*;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class ChangeOrderType implements Command{
    @Autowired
    private JwtDecoderService jwtDecoderService;

    private CartRepo cartRepo;

    @Autowired
    public ChangeOrderType(CartRepo cartRepo, JwtDecoderService jwtDecoderService, PromoRepo promoRepo, UserUsedPromoRepo userUsedPromoRepo) {
        this.cartRepo=cartRepo;
        this.jwtDecoderService=jwtDecoderService;
    }

    @Override
    public Object execute(Map<String,Object> data) throws Exception {
        String token = (String)data.get("token");
        Claims claims = jwtDecoderService.decodeJwtToken(token, "ziad1234aaaa&&&&&thisisasecretekeyaaa");

        if(claims == null) return "Invalid Token";

        String user = (String)claims.get("userId");
        UUID userID = UUID.fromString(user);

        String itemString = (String) data.get("itemId");
        UUID itemID = UUID.fromString(itemString);

        String orderType = (String) data.get("orderType");


        CartTable userCart = cartRepo.getCart(userID);

        if(userCart != null){
            if(orderType.equals("shipping")){
                CartItem cartItem = userCart.getItems().stream()
                        .filter(item -> item.getItemId().equals(itemID)).
                        toList().get(0);
                cartItem.setDeliveryType("shipping");
            }else{
                userCart.getItems().forEach(item -> item.setDeliveryType(orderType));
            }
            cartRepo.updateCartItems(userCart.getItems(), userCart.getId(), userCart.getTotalAmount());
            return cartRepo.getCart(userID).toString();
        }else{
            return "Cart not found";
        }

    }
}
