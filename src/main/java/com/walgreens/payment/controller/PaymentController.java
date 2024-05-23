package com.walgreens.payment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.Stripe;
import com.walgreens.payment.model.Enums.Duration;
import com.walgreens.payment.service.command.*;


import com.walgreens.payment.service.kafka.KafkaPublisher;
import com.walgreens.payment.service.kafka.message.keys.Keys;
import com.walgreens.payment.utils.JwtUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.web.client.HttpClientErrorException;


@RestController
@RequestMapping("/stripe")
public class PaymentController {


    private final CreateCustomerCommand createCustomerCommand;
    private final AddPaymentMethodCommand addPaymentMethodCommand;
    private final ViewPaymentMethodsCommand viewPaymentMethodsCommand;
    private final ViewBalanceCommand viewBalanceCommand;
    private final ViewLoyaltyPointsCommand viewLoyaltyPointsCommand;
    private final CreateCheckoutCommand createCheckoutCommand;
    private final PayUsingPaymentMethodsCommand payUsingPaymentMethodsCommand;
    private final CreateCouponCommand createCouponCommand;


    private final WebhookService webhookService;

    @Autowired
    private KafkaPublisher kafkaPublisher;



    @Autowired
    public PaymentController(CreateCustomerCommand createCustomerCommand,
                             AddPaymentMethodCommand addPaymentMethodCommand,
                             ViewPaymentMethodsCommand viewPaymentMethodsCommand,
                             ViewBalanceCommand viewBalanceCommand,
                             ViewLoyaltyPointsCommand viewLoyaltyPointsCommand,
                             CreateCheckoutCommand createCheckoutCommand,
                             CreateCouponCommand createCouponCommand,
                             PayUsingPaymentMethodsCommand payUsingPaymentMethodsCommand,

                             WebhookService webhookService
    ) {
        this.createCustomerCommand = createCustomerCommand;
        this.addPaymentMethodCommand = addPaymentMethodCommand;
        this.viewPaymentMethodsCommand = viewPaymentMethodsCommand;
        this.viewBalanceCommand = viewBalanceCommand;
        this.viewLoyaltyPointsCommand = viewLoyaltyPointsCommand;
        this.createCheckoutCommand = createCheckoutCommand;
        this.createCouponCommand = createCouponCommand;
        this.payUsingPaymentMethodsCommand = payUsingPaymentMethodsCommand;
        this.webhookService = webhookService;
    }



    @Value("${api.stripe.key}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {

        Stripe.apiKey = stripeApiKey;
    }


    @PostMapping("/createToken")
    public String createToken(UUID userUuid) {

        String token = JwtUtil.generateToken(String.valueOf(userUuid),"");
        return token;
    }


    @PostMapping("/testZiad")
    public String testZiad(String payload) {
//         "{\"request\":\"CheckPaymentMethod\",\"commandName\":\"ProceedToCheckOutCommand\",\"customerUuid\":\"f32ac0db-89b8-4cd3-a967-0eb7c656c1a1\",\"cartItems\":[{\"deliveryType\":\"standard\",\"comment\":\"Great product\",\"itemCount\":5,\"itemName\":null,\"purchasedPrice\":10.99,\"itemId\":\"5ef821aa-5558-405d-ba17-9cf114cd6c26\"}],\"paymentAmount\":\"49.45545\",\"userId\":\"f32ac0db-89b8-4cd3-a967-0eb7c656c1a1\",\"cartUuid\":\"a3b8a792-41c4-4b04-919e-8ea7a28ddfef\"}"
        //payload = "{\"request\":\"CheckPaymentMethod\",\"commandName\":\"ProceedToCheckOutCommand\",\"customerUuid\":\"f32ac0db-89b8-4cd3-a967-0eb7c656c1a1\",\"cartItems\":[{\"deliveryType\":\"standard\",\"comment\":\"Great product\",\"itemCount\":2,\"itemId\":\"5ef821aa-5558-405d-ba17-9cf114cd6c26\",\"purchasedPrice\":10.99,\"itemName\":\"Shampoo\"}],\"paymentAmount\":\"16.98\",\"userId\":\"f32ac0db-89b8-4cd3-a967-0eb7c656c1a1\",\"cartUuid\":\"a3b8a792-41c4-4b04-919e-8ea7a28ddfef\"}";
        payload=payload.replace("\\", "");
        payload=payload.substring(1, payload.length()-1);
        kafkaPublisher.publish("payment", payload);
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            Map<String, Object> map = objectMapper.readValue(payload, new TypeReference<Map<String, Object>>() {});
//            System.out.println(map);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //call commands
        return "success";
    }
    @PostMapping("/createCustomer")
    public String createCustomer(String sessionID, String name, String email) {

        String customerID = JwtUtil.getUserIdFromToken(sessionID);
        if(customerID == null){
            return "Session is invalid";
        }
        UUID customerUuid = UUID.fromString(customerID);
        System.out.println(customerUuid);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;

        try {
            jsonString = objectMapper.writeValueAsString(
                    Map.of(
                            "request", "CreateCustomer",
                            Keys.customerUuid, customerUuid,
                            Keys.customerName, name,
                            Keys.customerEmail, email
                    )
            );

            kafkaPublisher.publish("payment", jsonString);
            return "success";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
//        createCustomerCommand.setCustomerUuid(customerUuid);
//        createCustomerCommand.setName(name);
//        createCustomerCommand.setEmail(email);
//        createCustomerCommand.execute();
    }


    @PostMapping("/addPaymentMethod")
    public String addPaymentMethod(String sessionID, String cardNumber, String expiryMonth, String expiryYear, String cvv, String cardholderName, boolean isDefault, boolean hasFunds) {


        String customerID = JwtUtil.getUserIdFromToken(sessionID);
        if(customerID == null){
            return "Session is invalid";
        }
        UUID customerUuid = UUID.fromString(customerID);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(
                    Map.of(
                            "request", "AddPaymentMethod",
                            Keys.customerUuid, customerUuid,
                            Keys.cardNumber, cardNumber,
                            Keys.expiryMonth, expiryMonth,
                            Keys.expiryYear, expiryYear,
                            Keys.cvv, cvv,
                            Keys.cardholderName, cardholderName,
                            Keys.isDefault, isDefault,
                            Keys.hasFunds, hasFunds
                    )
            );
            kafkaPublisher.publish("payment", jsonString);
            return "success";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
//        addPaymentMethodCommand.setCustomerUuid(customerUuid);
//        addPaymentMethodCommand.setCardNumber(cardNumber);
//        addPaymentMethodCommand.setExpiryMonth(expiryMonth);
//        addPaymentMethodCommand.setExpiryYear(expiryYear);
//        addPaymentMethodCommand.setCvv(cvv);
//        addPaymentMethodCommand.setCardholderName(cardholderName);
//        addPaymentMethodCommand.setIsDefault(isDefault);
//        addPaymentMethodCommand.setIsDefault(isDefault);
//        addPaymentMethodCommand.execute();
    }

    @PostMapping("/viewPaymentMethods")
    public Object viewPaymentMethods(String sessionID) {

        String customerID = JwtUtil.getUserIdFromToken(sessionID);
        if(customerID == null){
            return "Session is invalid";
        }
        UUID customerUuid = UUID.fromString(customerID);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(Map.of(
                            "request", "ViewPaymentMethods",
                            Keys.customerUuid, customerUuid
                    )
            );
            kafkaPublisher.publish("payment", jsonString);
            return "success";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getMessage();
        }


//        viewPaymentMethodsCommand.setCustomerUuid(customerUuid);
//        viewPaymentMethodsCommand.execute();
    }

    @PostMapping("/viewBalance")
    public String viewBalance(String sessionID) {

        String customerID = JwtUtil.getUserIdFromToken(sessionID);
        if(customerID == null){
            return "Session is invalid";
        }
        UUID customerUuid = UUID.fromString(customerID);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(
                    Map.of(
                            "request", "ViewBalance",
                            Keys.customerUuid, customerUuid)

            );
            kafkaPublisher.publish("payment", jsonString);
            return "success";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getMessage();
        }

//        viewBalanceCommand.setCustomerUuid(customerUuid);
//        viewBalanceCommand.execute();
    }

    @PostMapping("/viewLoyaltyPoints")
    public String viewLoyaltyPoints(String sessionID) {

        String customerID = JwtUtil.getUserIdFromToken(sessionID);
        if(customerID == null){
            return "Session is invalid";
        }
        UUID customerUuid = UUID.fromString(customerID);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(
                    Map.of(
                            "request", "ViewLoyaltyPoints",
                            Keys.customerUuid, customerUuid)

            );
            kafkaPublisher.publish("payment", jsonString);
            return "success";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getMessage();
        }

//        viewLoyaltyPointsCommand.setCustomerUuid(customerUuid);
//        viewLoyaltyPointsCommand.execute();
    }

    @PostMapping("/createCheckout")
    public String createCheckout(String sessionID, UUID couponUuid) {

        String customerID = JwtUtil.getUserIdFromToken(sessionID);
        if(customerID == null){
            return "Session is invalid";
        }
        UUID customerUuid = UUID.fromString(customerID);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(
                    Map.of(
                            "request", "CreateCheckout",
                            Keys.customerUuid, customerUuid,
                            Keys.couponUuid, couponUuid

                    )

            );
            kafkaPublisher.publish("payment", jsonString);
            return "success";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getMessage();
        }

//        createCheckoutCommand.setCustomerUuid(customerUuid);
//        createCheckoutCommand.setCouponUuid(couponUuid);
//        createCheckoutCommand.execute();
    }

    @PostMapping("/createCoupon")
    public String createCoupon(String name, BigDecimal percentOff, Duration duration, Long duration_in_months) {

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(
                    Map.of(
                            "request", "CreateCoupon",
                            Keys.couponName, name,
                            Keys.percentOff, percentOff,
                            Keys.duration, duration,
                            Keys.duration_in_months, duration_in_months
                    )
            );
            kafkaPublisher.publish("payment", jsonString);
            return "success";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getMessage();
        }

//        createCouponCommand.setName(name);
//        createCouponCommand.setPercentOff(percentOff);
//        createCouponCommand.setDuration(duration);
//        createCouponCommand.setDuration_in_months(duration_in_months);
//        createCouponCommand.execute();
    }

    @PostMapping("/payUsingPaymentMethod")
    public String payUsingPaymentMethod(String sessionID, UUID cartUuid, UUID paymentMethodUuid, Double amount) {

        String customerID = JwtUtil.getUserIdFromToken(sessionID);
        if(customerID == null){
            return "Session is invalid";
        }
        UUID customerUuid = UUID.fromString(customerID);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(
                    Map.of(
                            "request", "PayUsingPaymentMethods",
                            Keys.customerUuid, customerUuid,
                            Keys.cartUuid, cartUuid,
                            Keys.paymentAmount, amount,

                            Keys.paymentMethodUuid, paymentMethodUuid
                    )
            );
            kafkaPublisher.publish("payment", jsonString);
            return "success";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.getMessage();
        }
//        payUsingPaymentMethodsCommand.setCustomerUuid(customerUuid);
//        payUsingPaymentMethodsCommand.setCartUuid(cartUuid);
//        payUsingPaymentMethodsCommand.setPaymentMethodUuid(paymentMethodUuid);
//        payUsingPaymentMethodsCommand.setAmount(amount);
//        payUsingPaymentMethodsCommand.execute();
    }


    @PostMapping("/webhook")
    public void handleStripeEvent(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        webhookService.setPayload(payload);
        webhookService.setSigHeader(sigHeader);
        ResponseEntity<String> stringResponseEntity = webhookService.handleEvent();
        System.out.println(stringResponseEntity.toString());

    }
}
