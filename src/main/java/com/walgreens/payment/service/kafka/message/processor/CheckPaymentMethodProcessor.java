package com.walgreens.payment.service.kafka.message.processor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
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
        Map<String, Object> message = getMessageInfo();
        System.out.println(message.get(Keys.customerUuid));
        System.out.println("the message: "+message);
        checkPaymentMethodCommand.setCustomerUuid(UUID.fromString((String) message.get(Keys.customerUuid)));
        checkPaymentMethodCommand.setCartUuid(UUID.fromString((String) message.get(Keys.cartUuid)));
        checkPaymentMethodCommand.setAmount(Double.parseDouble((String) message.get(Keys.paymentAmount)));
        checkPaymentMethodCommand.setSessionIdCart((String) message.get("sessionId"));

        ObjectMapper mapper = new ObjectMapper();
        try {
            // Assuming this is a JSON string of an array of CartItem objects
            String cartItemsJson = (String) message.get(Keys.cartItems);

            // Deserialize JSON string into a List of CartItem objects
            JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, CartItem.class);
            List<CartItem> cartItems = mapper.readValue(cartItemsJson, type);

            // Set the deserialized list to the command object
            checkPaymentMethodCommand.setCartItems(cartItems);

        } catch (Exception e) {
            e.printStackTrace();  // Proper error handling
        }
    }
}
