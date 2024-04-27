package com.walgreens.payment.service.command;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.WebhookEndpointCreateParams;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebhookService {

    @Value("${stripe.endpoint.secret}")
    private String endpointSecret;

    private UUID customerUuid;
    private UUID orderUuid;


    private String payload;
    private String sigHeader;
    private Event event;

    public ResponseEntity<String> handleEvent() {
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

            if ("checkout.session.completed".equals(event.getType())) {
                Optional<StripeObject> eventData = event.getDataObjectDeserializer().getObject();
                if (eventData.isPresent() && eventData.get() instanceof Session) {
                    Session sessionEvent = (Session) eventData.get();
                    String paymentIntentId = sessionEvent.getPaymentIntent();

                    PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

                    Map<String, String> metadata = paymentIntent.getMetadata();
                    orderUuid = UUID.fromString(metadata.get("order_uuid"));
                    customerUuid = UUID.fromString(metadata.get("customer_uuid"));

                    double amount = sessionEvent.getAmountSubtotal();
                    System.out.println("WEBHOOK CUSTOMERUUID: " + customerUuid);
                    System.out.println("WEBHOOK ORDERUUID: " + orderUuid);
                    System.out.println("WEBHOOK AMOUNT: " + amount);

                } else {
                    return ResponseEntity.status(400).body("Error deserializing session data");
                }

            }
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().body("Invalid signature");
        } catch (StripeException e){
            return ResponseEntity.badRequest().body("Invalid Payment Intent");
        } catch(Exception e) {
            return ResponseEntity.badRequest().body("Invalid Payload");
        }
        return ResponseEntity.ok("Received");
    }

}
