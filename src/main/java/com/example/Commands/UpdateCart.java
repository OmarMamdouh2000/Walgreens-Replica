package com.example.Commands;

import com.example.Cache.SessionCache;
import com.example.Final.CartRepo;
import com.example.Final.CartTable;
import com.example.Final.PromoRepo;
import com.example.Final.UserUsedPromoRepo;
import com.example.Kafka.KafkaProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UpdateCart implements Command{

    @Autowired
    private JwtDecoderService jwtDecoderService;

    private CartRepo cartRepo;

    private KafkaProducer kafkaProducer;

    private ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;

    @Autowired
    private SessionCache sessionCache;

    @Autowired
    public UpdateCart(CartRepo cartRepo, JwtDecoderService jwtDecoderService, PromoRepo promoRepo, UserUsedPromoRepo userUsedPromoRepo, KafkaProducer kafkaProducer, SessionCache sessionCache,ReplyingKafkaTemplate<String, Message<String>, Message<String>> replyingKafkaTemplate) {
        this.cartRepo=cartRepo;
        this.jwtDecoderService=jwtDecoderService;
        this.kafkaProducer = kafkaProducer;
        this.sessionCache = sessionCache;
    }

    @Override
    public Object execute(Map<String, Object> data) {
        System.out.println("UPDATING CART IN DB");

        ObjectMapper objectMapper = new ObjectMapper();
        CartTable cart = objectMapper.convertValue(data.get("cart"), CartTable.class);

        cartRepo.save(cart);
        return "Cart Updated in DB Successfully";
    }

}
