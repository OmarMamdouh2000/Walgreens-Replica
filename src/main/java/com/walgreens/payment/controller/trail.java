//PaymentController.java
//        ```
//        package org.scalablebugbusters.paymentservice.payment.controller;
//
//import com.stripe.exception.StripeException;
//import lombok.RequiredArgsConstructor;
//import org.scalablebugbusters.paymentservice.payment.dto.CheckoutSessionResponse;
//import org.scalablebugbusters.paymentservice.payment.dto.CoursePurchaseRequest;
//import org.scalablebugbusters.paymentservice.payment.exceptions.PaymentAlreadyExistsException;
//import org.scalablebugbusters.paymentservice.payment.service.CheckoutService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RestController
//@RequestMapping("/payments")
//@RequiredArgsConstructor
//public class PaymentController {
//    private final CheckoutService checkoutService;
//
//    @PostMapping("/checkout")
//    public ResponseEntity<CheckoutSessionResponse> createCheckoutSession(CoursePurchaseRequest request) {
//        try {
//            String sessionUrl = checkoutService.initiateCheckout(request).getUrl();
//            return ResponseEntity.ok(new CheckoutSessionResponse(sessionUrl, null));
//        } catch (PaymentAlreadyExistsException e) {
//            return ResponseEntity.status(400).body(new CheckoutSessionResponse(null, new String[]{e.getMessage()}));
//        } catch (StripeException e) {
//            return ResponseEntity.status(500).body(new CheckoutSessionResponse(null, new String[]{e.getMessage()}));
//        }
//    }
//}
//```
//
//
//
//StripeWebhookController.java
//
//```
//        package org.scalablebugbusters.paymentservice.payment.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.scalablebugbusters.paymentservice.payment.service.StripeWebhookService;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//public class StripeWebhookController {
//    private final StripeWebhookService stripeWebhookService;
//
//    @PostMapping("/stripe/events")
//    public String handleStripeEvent(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
//        stripeWebhookService.handleEvent(payload, sigHeader);
//
//        return "";
//    }
//}
//```
//
//
//CheckoutSessionResponse.java
//
//```
//        package org.scalablebugbusters.paymentservice.payment.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//
//@Data
//@AllArgsConstructor
//public class CheckoutSessionResponse {
//    private String sessionUrl;
//    private String[] errors;
//}
//```