package com.example.Commands;

import com.example.Final.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RemoveItem implements Command{
    @Autowired
    private JwtDecoderService jwtDecoderService;

    private CartRepo cartRepo;

    @Autowired
    public RemoveItem(CartRepo cartRepo, JwtDecoderService jwtDecoderService, PromoRepo promoRepo, UserUsedPromoRepo userUsedPromoRepo) {
        this.cartRepo=cartRepo;
        this.jwtDecoderService=jwtDecoderService;
    }

    @Override
    public Object execute(Map<String,Object> data) throws Exception {
        String user = (String)data.get("User");
        String itemString = (String)data.get("Item");
        UUID userId = UUID.fromString(user);
        UUID itemId = UUID.fromString(itemString);

        CartTable userCart = cartRepo.getCart(userId);

        if(userCart != null){
            CartItem toBeDeleted = userCart.getItems().stream()
                    .filter(item -> item.getItemId().equals(itemId))
                    .findFirst()
                    .orElse(null);

            double priceRemove = toBeDeleted.getPurchasedPrice() * toBeDeleted.getItemCount();

            List<CartItem> updatedItems = userCart.getItems().stream()
                    .filter(item -> !item.getItemId().equals(itemId))
                    .collect(Collectors.toList());


            userCart.setItems(updatedItems);
            userCart.setTotalAmount(userCart.getTotalAmount() - priceRemove);
            cartRepo.updateCartItems(userCart.getItems(), userCart.getId(), userCart.getTotalAmount());

            return new ResponseEntity<>(cartRepo.getCart(userId), HttpStatus.OK);
        }else{
            throw new Exception("Cart not found");
        }

    }
}
