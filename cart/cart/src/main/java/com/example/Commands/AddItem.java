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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class AddItem implements Command{

    @Autowired
    private JwtDecoderService jwtDecoderService;

    private CartRepo cartRepo;

    private PromoRepo promoRepo;

    private UserUsedPromoRepo userUsedPromoRepo;

    @Autowired
	private SessionCache sessionCache;
    @Autowired
    private ReplyingKafkaTemplate<String, Message<String>, Message<String>> replyingKafkaTemplate;

    @Autowired
    public AddItem(CartRepo cartRepo, JwtDecoderService jwtDecoderService, PromoRepo promoRepo, UserUsedPromoRepo userUsedPromoRepo, KafkaProducer kafkaProducer, SessionCache sessionCache,ReplyingKafkaTemplate<String, Message<String>, Message<String>> replyingKafkaTemplate) {
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
        String itemId = (String) data.get("itemId");
        
        int itemCount = (int) data.get("itemCount");
        String itemName=(String) data.get("itemName");

        double itemPrice = (double) data.get("itemPrice");
        String deliveryType = (String) data.get("deliveryType");
        double discount = Double.parseDouble((String)data.get("discount"));
        itemPrice = itemPrice * (1 - discount / 100);

        CartTable Cart;
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> cachedCart = sessionCache.getSessionSection(sessionId, "cart");

        if(!cachedCart.isEmpty()) Cart = objectMapper.convertValue(cachedCart, CartTable.class);
        else Cart = cartRepo.getCart(userId);
        boolean foundCart=false;
        if (Cart == null){
            Cart=new CartTable();
            foundCart=true;
        } 
            

        List<CartItem> Cart_Items = Cart.getItems()!=null?Cart.getItems():new ArrayList<CartItem>();
        CartItem first_item = Cart_Items!=null && Cart_Items.size()!=0?Cart_Items.get(0):null;
        double newTotal = Cart.getTotalAmount();

        if(first_item!=null){
            String first_item_deliveryType = first_item.getDeliveryType();
            CartItem item_to_be_added = new CartItem();
            System.out.println(itemId);
            UUID itemIdUUID = UUID.fromString(itemId);
            item_to_be_added.setItemId(itemIdUUID);
            item_to_be_added.setItemCount(itemCount);
            item_to_be_added.setDeliveryType(first_item_deliveryType);
            item_to_be_added.setPurchasedPrice(itemPrice);
            item_to_be_added.setItemName(itemName);
            Cart_Items.add(item_to_be_added);
            if(Objects.equals(Cart.getAppliedPromoCodeId(), ' ')) {
                double newAmount = itemPrice * (1 - Cart.getPromoCodeAmount() / 100);
                newTotal += newAmount * itemCount;
            }else{
                newTotal += itemCount*itemPrice;
            }
            Cart.setTotalAmount(newTotal);
        }
        else {
            CartItem item_to_be_added = new CartItem();
            item_to_be_added.setItemId(UUID.fromString(itemId));
            item_to_be_added.setItemCount(itemCount);
            item_to_be_added.setDeliveryType(deliveryType);
            item_to_be_added.setPurchasedPrice(itemPrice);
            item_to_be_added.setItemName(itemName);
            Cart_Items.add(item_to_be_added);
            if(Objects.equals(Cart.getAppliedPromoCodeId(), ' ')) {
                double newAmount = itemPrice * (1 - Cart.getPromoCodeAmount() / 100);
                newTotal += newAmount * itemCount;
            }else{
                newTotal += itemCount*itemPrice;
            }
            Cart.setTotalAmount(newTotal);
        }
        if(foundCart){
            Cart.setUserId(userId);
            Cart.setItems(Cart_Items);
            Cart.setId(UUID.randomUUID());
            Cart.setSavedForLaterItems(new ArrayList<CartItem>());
            Cart.setAppliedPromoCodeId("");
            Cart.setPromoCodeAmount(0);
            cartRepo.save(Cart);
        }
        

        String jsonString = objectMapper.writeValueAsString(Cart);
        Map<String, Object> map = objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {});
        sessionCache.updateSessionSection(sessionId, "cart", map, 10, TimeUnit.HOURS);

        return Cart;
    }
}
