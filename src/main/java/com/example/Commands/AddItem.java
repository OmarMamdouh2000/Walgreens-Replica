package com.example.Commands;

import com.example.Cache.SessionCache;
import com.example.Final.*;
import com.example.Kafka.KafkaProducer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public AddItem(CartRepo cartRepo, JwtDecoderService jwtDecoderService, PromoRepo promoRepo, UserUsedPromoRepo userUsedPromoRepo, KafkaProducer kafkaProducer, SessionCache sessionCache) {
        this.cartRepo=cartRepo;
        this.jwtDecoderService=jwtDecoderService;
        this.promoRepo=promoRepo;
        this.userUsedPromoRepo=userUsedPromoRepo;
        this.sessionCache = sessionCache;
    }
    @Override
    public Object execute(Map<String, Object> data) throws Exception {
        String sessionId = (String) data.get("sessionId");

        //TODO: data is the product from products
        String user=(String)data.get("userId");
        if(user==null)
            return "User not found or Invalid Token";
        UUID userId = UUID.fromString(user);

        String itemId = (String) data.get("itemId");
        int itemCount = (int) data.get("itemCount");

        double itemPrice = (double) data.get("itemPrice");
        String deliveryType = (String) data.get("deliveryType");
        double discount = (double) data.get("discount");
        itemPrice = itemPrice * (1 - discount / 100);

        CartTable Cart;
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> cachedCart = sessionCache.getSessionSection(sessionId, "cart");

        if(!cachedCart.isEmpty()) Cart = objectMapper.convertValue(cachedCart, CartTable.class);
        else Cart = cartRepo.getCart(userId);

        if (Cart == null) return "Cart not found";

        List<CartItem> Cart_Items = Cart.getItems();
        CartItem first_item = Cart_Items.get(0);
        double newTotal = Cart.getTotalAmount();

        if(first_item!=null){
            String first_item_deliveryType = first_item.getDeliveryType();
            CartItem item_to_be_added = new CartItem();
            UUID itemIdUUID = UUID.fromString(itemId);
            item_to_be_added.setItemId(itemIdUUID);
            item_to_be_added.setItemCount(itemCount);
            item_to_be_added.setDeliveryType(first_item_deliveryType);
            item_to_be_added.setPurchasedPrice(itemPrice);
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
            Cart_Items.add(item_to_be_added);
            if(Objects.equals(Cart.getAppliedPromoCodeId(), ' ')) {
                double newAmount = itemPrice * (1 - Cart.getPromoCodeAmount() / 100);
                newTotal += newAmount * itemCount;
            }else{
                newTotal += itemCount*itemPrice;
            }
            Cart.setTotalAmount(newTotal);
        }

        String jsonString = objectMapper.writeValueAsString(Cart);
        Map<String, Object> map = objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {});
        sessionCache.updateSessionSection(sessionId, "cart", map, 10, TimeUnit.HOURS);

        return Cart;
    }
}
