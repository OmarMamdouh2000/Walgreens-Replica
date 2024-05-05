package com.example.Commands;

import com.example.Final.*;
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

        String user = (String)data.get("User");
        String itemString = (String) ((Map<String, Object>)data.get("Data")).get("itemId");
        String orderType = (String) ((Map<String, Object>)data.get("Data")).get("orderType");

        UUID userID = UUID.fromString(user);
        UUID itemID = UUID.fromString(itemString);

        CartTable userCart = cartRepo.getCart(userID);
        System.out.println("USERCART:  " + userCart);
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
            return  new ResponseEntity<> (cartRepo.getCart(userID), HttpStatus.OK);
        }else{
            throw new Exception("Cart not found");
        }

    }
}
