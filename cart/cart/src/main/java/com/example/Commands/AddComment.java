package com.example.Commands;

import com.example.Cache.SessionCache;
import com.example.Final.*;
import com.example.Kafka.KafkaProducer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class AddComment implements Command{
    @Autowired
    private JwtDecoderService jwtDecoderService;

    private CartRepo cartRepo;

    private PromoRepo promoRepo;

    private UserUsedPromoRepo userUsedPromoRepo;

    @Autowired
	private SessionCache sessionCache;
    ReplyingKafkaTemplate<String, Message<String>, Message<String>> replyingKafkaTemplate;
    @Autowired
    public AddComment(CartRepo cartRepo, JwtDecoderService jwtDecoderService, PromoRepo promoRepo, UserUsedPromoRepo userUsedPromoRepo, KafkaProducer kafkaProducer, SessionCache sessionCache,ReplyingKafkaTemplate<String, Message<String>, Message<String>> replyingKafkaTemplate) {
        this.cartRepo=cartRepo;
        this.jwtDecoderService=jwtDecoderService;
        this.promoRepo=promoRepo;
        this.userUsedPromoRepo=userUsedPromoRepo;
        this.sessionCache = sessionCache;
        this.replyingKafkaTemplate=replyingKafkaTemplate;
    }

    @Override
    public Object execute(Map<String, Object> data) throws Exception {
        String sessionId = (String) data.get("sessionId");

        String user=(String)data.get("userId");
        if(user==null)
            return "User not found or Invalid Token";
        UUID userId = UUID.fromString(user);
        

        String itemString = (String) data.get("itemId");
        UUID itemID = UUID.fromString(itemString);

        String comment = (String) data.get("comment");

        CartTable Cart;
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> cachedCart = sessionCache.getSessionSection(sessionId, "cart");

        if(!cachedCart.isEmpty()) Cart = objectMapper.convertValue(cachedCart, CartTable.class);
        else Cart = cartRepo.getCart(userId);

        if (Cart == null) return "Cart not found";

        List<CartItem> Cart_Items = Cart.getItems();
        CartItem cartItem = Cart_Items.stream()
                .filter(item -> item.getItemId().equals(itemID)).
                toList().get(0);
        cartItem.setComment(comment);

        String jsonString = objectMapper.writeValueAsString(Cart);
        Map<String, Object> map = objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {});
        sessionCache.updateSessionSection(sessionId, "cart", map, 10, TimeUnit.HOURS);

        return Cart;
        
    }
}
