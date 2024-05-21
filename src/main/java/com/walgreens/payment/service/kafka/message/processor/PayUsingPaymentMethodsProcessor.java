package com.walgreens.payment.service.kafka.message.processor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walgreens.payment.model.CartItem;
import com.walgreens.payment.service.command.PayUsingPaymentMethodsCommand;
import com.walgreens.payment.service.kafka.message.keys.Keys;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PayUsingPaymentMethodsProcessor extends Processor{

    @Override
    public void process() {

        PayUsingPaymentMethodsCommand payUsingPaymentMethodsCommand = (PayUsingPaymentMethodsCommand) getCommand();
        ObjectMapper mapper = new ObjectMapper();

        Map<String, String> message = getMessageInfo();


        payUsingPaymentMethodsCommand.setCustomerUuid(UUID.fromString(message.get(Keys.customerUuid)));

        payUsingPaymentMethodsCommand.setCartUuid(UUID.fromString(message.get(Keys.cartUuid)));

        payUsingPaymentMethodsCommand.setPaymentMethodUuid(UUID.fromString(message.get(Keys.paymentMethodUuid)));
        payUsingPaymentMethodsCommand.setAmount(Double.valueOf(message.get(Keys.paymentAmount)));

        try {
            String cartItemsJson = message.get(Keys.cartItems);
            List<CartItem> cartItems = mapper.readValue(cartItemsJson, new TypeReference<List<CartItem>>() {});
            payUsingPaymentMethodsCommand.setCartItems(cartItems);
        } catch (IOException e) {
            e.printStackTrace();  // Handle exceptions appropriately
        };

    }
}
