package com.example.Commands;

import com.example.Final.*;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AddComment implements Command{
    @Autowired
    private JwtDecoderService jwtDecoderService;

    private CartRepo cartRepo;

    private PromoRepo promoRepo;

    private UserUsedPromoRepo userUsedPromoRepo;

    @Autowired
    public AddComment(CartRepo cartRepo, JwtDecoderService jwtDecoderService, PromoRepo promoRepo, UserUsedPromoRepo userUsedPromoRepo) {
        this.cartRepo=cartRepo;
        this.jwtDecoderService=jwtDecoderService;
        this.promoRepo=promoRepo;
        this.userUsedPromoRepo=userUsedPromoRepo;
    }

    @Override
    public Object execute(Map<String, Object> data) throws Exception {
        String secretKey = "ziad1234aaaa&&&&&thisisasecretekeyaaa";
        String token = (String) data.get("token");
        String itemString = (String) data.get("itemId");
        UUID itemID = UUID.fromString(itemString);
        String comment = (String) data.get("comment");
        Claims claims = jwtDecoderService.decodeJwtToken(token, secretKey);

        if(claims == null) return "Invalid token";
        else{
            String user=(String) claims.get("userId");
            UUID userId = UUID.fromString(user);
            CartTable Cart = cartRepo.getCart(userId);
            UUID cartId = Cart.getId();
            List<CartItem> Cart_Items = Cart.getItems();
            CartItem cartItem = Cart_Items.stream()
                    .filter(item -> item.getItemId().equals(itemID)).
                    toList().get(0);
            cartItem.setComment(comment);

            cartRepo.updateCartItems(Cart_Items, cartId, Cart.getTotalAmount());
            return cartRepo.getCart(userId);
        }
    }
}
