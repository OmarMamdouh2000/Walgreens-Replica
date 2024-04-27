package com.walgreens.payment.service.command;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentMethodCreateParams;
import com.walgreens.payment.repository.CustomerRepository;
import com.walgreens.payment.repository.PaymentMethodsRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddPaymentMethodCommandOLD implements Command {

    private UUID customerUuid;
    private int type;
    boolean isDefault;

    @Value("${api.stripe.key}")
    private String stripeApiKey;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PaymentMethodsRepository paymentMethodsRepository;


    @Override
    public void execute() {

        try {

            PaymentMethodCreateParams.Builder paymentMethodCreateParamsBuilder;
            switch (type){
                case 0:
                    paymentMethodCreateParamsBuilder =
                            PaymentMethodCreateParams.builder()
                                    .setPaymentMethod("pm_card_visa_chargeDeclinedInsufficientFunds");
                    break;
                case 1:
                    paymentMethodCreateParamsBuilder =
                            PaymentMethodCreateParams.builder()
                                    .setPaymentMethod("pm_card_visa");
                    break;
                case 2:
                    paymentMethodCreateParamsBuilder =
                            PaymentMethodCreateParams.builder()
                                    .setPaymentMethod("pm_card_visa_debit");
                    break;
                default:
                    paymentMethodCreateParamsBuilder =
                            PaymentMethodCreateParams.builder()

                                    .setPaymentMethod("pm_card_chargeDeclinedExpiredCard");

            }

            RequestOptions requestOptions = RequestOptions.builder()
                    .setStripeAccount("acct_1P8PpqC0ZDZEI6HD")
                    .setApiKey(stripeApiKey)
                    .build();

            PaymentMethod paymentMethod = PaymentMethod.create(
                    paymentMethodCreateParamsBuilder.build(),
                    requestOptions

            );

            String customerId = customerRepository.get_customer(customerUuid);

            Customer customer = Customer.retrieve(customerId);

            if(isDefault || customer.getInvoiceSettings().getDefaultPaymentMethod() == null){
                customer.getInvoiceSettings().setDefaultPaymentMethod(paymentMethod.getId());
            }

            paymentMethod.setCustomer(customer.getId());


            // Calling the stored procedure and saving it to the database
            UUID paymentMethodID = UUID.randomUUID();
//            paymentMethodsRepository.create_payment_method(paymentMethodID, paymentMethod.getId());







        }catch (StripeException e){
            log.error("Exception createCustomerCommand", e);
        }




    }
}
