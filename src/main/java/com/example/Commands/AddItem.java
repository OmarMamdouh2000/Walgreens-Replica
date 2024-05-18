package com.example.Commands;

import com.example.Cache.SessionCache;
import com.example.Final.*;
import com.example.Kafka.KafkaProducer;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

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
        //TODO: data is the product from products
        String user=(String)data.get("userId");


        String itemId = (String) data.get("itemId");
        int itemCount = (int) data.get("itemCount");

        double itemPrice = (double) data.get("itemPrice");
        String deliveryType = (String) data.get("deliveryType");
        double discount = (double) data.get("discount");
        itemPrice = itemPrice * (1 - discount / 100);

        UUID userId = UUID.fromString(user);

        CartTable Cart = cartRepo.getCart(userId);
        UUID cartId = Cart.getId();

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
        }

        cartRepo.updateCartItems(Cart_Items, cartId, newTotal);
        return cartRepo.getCart(userId);
    }
}
