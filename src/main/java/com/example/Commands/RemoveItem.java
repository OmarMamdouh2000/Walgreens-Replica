package com.example.Commands;

import com.example.Cache.SessionCache;
import com.example.Final.*;
import com.example.Kafka.KafkaProducer;

import io.jsonwebtoken.Claims;
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

    private KafkaProducer kafkaProducer;

    @Autowired
	private SessionCache sessionCache;
    
    @Autowired
    public RemoveItem(CartRepo cartRepo, JwtDecoderService jwtDecoderService, PromoRepo promoRepo, UserUsedPromoRepo userUsedPromoRepo,KafkaProducer kafkaProducer, SessionCache sessionCache) {
    	this.cartRepo=cartRepo;
    	this.jwtDecoderService=jwtDecoderService;
        this.kafkaProducer = kafkaProducer;
        this.sessionCache = sessionCache;
    }

    @Override
    public Object execute(Map<String,Object> data) throws Exception {

        String user = (String)data.get("userId");
        UUID userId = UUID.fromString(user);

        String itemString = (String)data.get("itemId");
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

            return cartRepo.getCart(userId);
        }else{
            return "Cart not found";
        }

    }
}
