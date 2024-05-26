package com.example.Commands;

import com.example.Cache.SessionCache;
import com.example.Final.*;
import com.example.Kafka.KafkaProducer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ApplyPromo implements Command{
    @Autowired
    private JwtDecoderService jwtDecoderService;

    private CartRepo cartRepo;

    private PromoRepo promoRepo;

    private UserUsedPromoRepo userUsedPromoRepo;

    private KafkaProducer kafkaProducer;

    private ReplyingKafkaTemplate<String, Message<String>, Message<String>> replyingKafkaTemplate;

    @Autowired
	private SessionCache sessionCache;
    
    @Autowired
    public ApplyPromo(CartRepo cartRepo, JwtDecoderService jwtDecoderService, PromoRepo promoRepo, UserUsedPromoRepo userUsedPromoRepo,KafkaProducer kafkaProducer, SessionCache sessionCache,ReplyingKafkaTemplate<String, Message<String>, Message<String>> replyingKafkaTemplate) {
    	this.cartRepo=cartRepo;
    	this.jwtDecoderService=jwtDecoderService;
        this.kafkaProducer = kafkaProducer;
        this.sessionCache = sessionCache;
        this.promoRepo=promoRepo;
        this.userUsedPromoRepo=userUsedPromoRepo;
    }

    @Override
    public Object execute(Map<String,Object> data) throws Exception {
        String sessionId = (String) data.get("sessionId");

        String user=(String)data.get("userId");
        if(user==null)
            return "User not found or Invalid Token";
        UUID userID = UUID.fromString(user);

        String promoCode = (String) data.get("promoCode");

        //get promo from promos --> if not found return invalid
        PromoCodeTable promo = promoRepo.getPromoCodeByCode(promoCode);

        if (promo == null) {
            return "Promo not found";
        }else {
            CartTable userCart;
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> cachedCart = sessionCache.getSessionSection(sessionId, "cart");

            if(!cachedCart.isEmpty()) userCart = objectMapper.convertValue(cachedCart, CartTable.class);
            else userCart = cartRepo.getCart(userID);

            if (userCart == null) return "Cart not found";

            //check if promo is in userUsedPromos --> if so return invalid
            UserUsedPromo isUsed = userUsedPromoRepo.findUserPromo(userID, promoCode);

            if(!Objects.equals(userCart.getAppliedPromoCodeId(), ' ') && userCart.getPromoCodeAmount() != 0){
                return "Promo already applied";
            } else if (isUsed != null) {
                return "Promo already used";
            } else if (!promo.isValid()) {
                return "Promo not valid";
            } else if (promo.getExpiryDate().isBefore(java.time.LocalDate.now())) {
                return "Promo expired";
            } else {
                double totalAmount = userCart.getTotalAmount();
                double promoAmount = promo.getDiscountValue();

                //apply promo amount to total amount
                double newAmount = totalAmount * (1 - promoAmount / 100);
                userCart.setTotalAmount(newAmount);

                //apply promo to cart
                userCart.setAppliedPromoCodeId(promoCode);
                userCart.setPromoCodeAmount(promo.getDiscountValue());

                //add promo to userUsedPromos
                userUsedPromoRepo.insertUserPromo(userID, promoCode);

                String jsonString = objectMapper.writeValueAsString(userCart);
                Map<String, Object> map = objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {});
                sessionCache.updateSessionSection(sessionId, "cart", map, 10, TimeUnit.HOURS);

                return userCart;
            }
        }

    }


}
