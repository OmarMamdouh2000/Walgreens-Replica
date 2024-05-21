package com.walgreens.payment.service.kafka.message.processor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walgreens.payment.model.CartItem;
import com.walgreens.payment.service.command.CheckPaymentMethodCommand;
import com.walgreens.payment.service.command.CreateCheckoutCommand;
import com.walgreens.payment.service.kafka.message.keys.Keys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CheckPaymentMethodProcessor extends Processor{

    @Override
    public void process() {
        CheckPaymentMethodCommand checkPaymentMethodCommand = (CheckPaymentMethodCommand) getCommand();
        Map<String, String> message = getMessageInfo();
        System.out.println(message.get(Keys.customerUuid));
        System.out.println("the message: "+message);
        checkPaymentMethodCommand.setCustomerUuid(UUID.fromString(message.get(Keys.customerUuid)));
        checkPaymentMethodCommand.setCartUuid(UUID.fromString(message.get(Keys.cartUuid)));
        checkPaymentMethodCommand.setAmount(Double.parseDouble(message.get(Keys.paymentAmount)));
        ObjectMapper mapper = new ObjectMapper();
         try {
            String cartItemsJson = message.get(Keys.cartItems);
            System.out.println("aaaaaA: "+cartItemsJson);
            // Map<String,Object> map = mapper.readValue(cartItemsJson, Map.class);

            // List<CartItem> cartItems = (List<CartItem>) map.get("cartItems");
            checkPaymentMethodCommand.setCartItems(new ArrayList<CartItem>());
        } catch (Exception e) {
            e.printStackTrace();  // Handle exceptions appropriately
        };
    }
}
