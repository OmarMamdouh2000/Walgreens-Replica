package com.walgreens.payment.controller;

import com.stripe.Stripe;
import com.walgreens.payment.model.Enums.Duration;
import com.walgreens.payment.service.command.*;


import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;


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

    @PostMapping("/createCustomer")
    public void createCustomer(UUID customerUuid, String name, String email){
        createCustomerCommand.setCustomerUuid(customerUuid);
        createCustomerCommand.setName(name);
        createCustomerCommand.setEmail(email);
        createCustomerCommand.execute();

    }

    @PostMapping("/addPaymentMethod")
    public void addPaymentMethod(UUID customerUuid, String cardNumber, String expiryMonth, String expiryYear, String cvv, String cardholderName, boolean isDefault){
        addPaymentMethodCommand.setCustomerUuid(customerUuid);
        addPaymentMethodCommand.setCardNumber(cardNumber);
        addPaymentMethodCommand.setExpiryMonth(expiryMonth);
        addPaymentMethodCommand.setExpiryYear(expiryYear);
        addPaymentMethodCommand.setCvv(cvv);
        addPaymentMethodCommand.setCardholderName(cardholderName);
        addPaymentMethodCommand.setIsDefault(isDefault);
        addPaymentMethodCommand.setIsDefault(isDefault);
        addPaymentMethodCommand.execute();

    }

    @PostMapping("/viewPaymentMethods")
    public void viewPaymentMethods(UUID customerUuid){
        viewPaymentMethodsCommand.setCustomerUuid(customerUuid);
        viewPaymentMethodsCommand.execute();

    }

    @PostMapping("/viewBalance")
    public void viewBalance(UUID customerUuid){
        viewBalanceCommand.setCustomerUuid(customerUuid);
        viewBalanceCommand.execute();

    }

    @PostMapping("/viewLoyaltyPoints")
    public void viewLoyaltyPoints(UUID customerUuid){
        viewLoyaltyPointsCommand.setCustomerUuid(customerUuid);
        viewLoyaltyPointsCommand.execute();

    }

    @PostMapping("/createCheckout")
    public void createCheckout(UUID customerUuid, UUID couponUuid){
        createCheckoutCommand.setCustomerUuid(customerUuid);
        createCheckoutCommand.setCouponUuid(couponUuid);
        createCheckoutCommand.execute();

    }

    @PostMapping("/createCoupon")
    public void createCoupon(String name, BigDecimal percentOff, Duration duration, Long duration_in_months){
        createCouponCommand.setName(name);
        createCouponCommand.setPercentOff(percentOff);
        createCouponCommand.setDuration(duration);
        createCouponCommand.setDuration_in_months(duration_in_months);
        createCouponCommand.execute();

    }

    @PostMapping("/payUsingPaymentMethod")
    public void payUsingPaymentMethod(UUID customerUuid, UUID cartUuid, UUID paymentMethodUuid, Double amount){
        payUsingPaymentMethodsCommand.setCustomerUuid(customerUuid);
        payUsingPaymentMethodsCommand.setCartUuid(cartUuid);
        payUsingPaymentMethodsCommand.setPaymentMethodUuid(paymentMethodUuid);
        payUsingPaymentMethodsCommand.setAmount(amount);
        payUsingPaymentMethodsCommand.execute();

    }



    @PostMapping("/webhook")
    public void handleStripeEvent(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        webhookService.setPayload(payload);
        webhookService.setSigHeader(sigHeader);
        ResponseEntity<String> stringResponseEntity = webhookService.handleEvent();
        System.out.println(stringResponseEntity.toString());

    }





}
