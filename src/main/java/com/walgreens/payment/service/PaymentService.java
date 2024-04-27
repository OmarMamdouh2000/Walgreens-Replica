package com.walgreens.payment.service;

import com.stripe.exception.StripeException;
import com.stripe.model.LineItem;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.walgreens.payment.model.SessionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class PaymentService {
//    private final CommandMapper commandMapper;

//    public PaymentService(CommandMapper commandMapper) {
//        this.commandMapper = commandMapper;
//    }

//    public void invokeCommand(Request request) {
//        Command command = commandMapper.getCommand(request.getType());
//        command.execute();
//    }


    public SessionDto createPaymentSession(SessionDto sessionDto) {
        try {
            double amount = 23.00;

            String clientUrl = "https://localhost:4200";
            SessionCreateParams.Builder sessionCreateParamsBuilder =
                    SessionCreateParams.builder()
                            .setMode(SessionCreateParams.Mode.PAYMENT)
                            .setCustomer("")
                            .setSuccessUrl(clientUrl + "/sucess?session_id={CHECKOUT_SESSION_ID}")
                            .setCancelUrl(clientUrl + "/failure");

            sessionCreateParamsBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                    .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                            .putMetadata("cart_id", "123")
                                            .putMetadata("user_id", sessionDto.getUserId())
                                            .setName("Shoes XL")
                                            .build()
                                    )
                                    .setCurrency("USD")
                                    .setUnitAmountDecimal(BigDecimal.valueOf(amount * 100))
                                    .build()

                            )

                            .build()
            );

            SessionCreateParams.PaymentIntentData paymentIntentData =
                    SessionCreateParams.PaymentIntentData.builder()
                            .putMetadata("cart_id", "123")
                            .putMetadata("user_id", sessionDto.getUserId())
                            .build();

            sessionCreateParamsBuilder.setPaymentIntentData(paymentIntentData);

            Session session = Session.create(sessionCreateParamsBuilder.build());

            sessionDto.setSessionUrl(session.getUrl());
            sessionDto.setSessionId(session.getId());


            // FOR LATER USE
//            List<SessionCreateParams.LineItem> listOfItems = new ArrayList<SessionCreateParams.LineItem>();
//            listOfItems.add(
//                    SessionCreateParams.LineItem.builder()
//            )

            Session.create(sessionCreateParamsBuilder.build());
            return sessionDto;
        }catch(StripeException e){
            log.error("Exception createPaymentSession", e);
            sessionDto.setMessage(e.getMessage());


        }
        return sessionDto;
    }
}
