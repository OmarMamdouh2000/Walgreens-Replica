package com.walgreens.payment.service.command;

import com.walgreens.payment.controller.PaymentController;
import com.walgreens.payment.repository.PaymentMethodsRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jasypt.util.text.AES256TextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPaymentMethodCommand implements Command{

    private UUID paymentMethodUuid;
    private UUID customerUuid;
    private String cardNumber;
    private String expiryMonth;
    private String expiryYear;
    private String cvv;
    private String cardholderName;
    private Boolean isDefault;

    Logger logger= LoggerFactory.getLogger(AddPaymentMethodCommand.class);

    @Autowired
    private PaymentMethodsRepository paymentMethodsRepository;
    private AES256TextEncryptor textEncryptor;

    public String encryptCardDetails(String cardNumber) {
        return textEncryptor.encrypt(cardNumber);
    }

    public String decryptCardDetails(String encryptedCardNumber) {
        return textEncryptor.decrypt(encryptedCardNumber);
    }


    @Override
    public void execute() {
        expiryMonth = expiryMonth.substring(expiryMonth.length() - 2);
        expiryYear = expiryYear.substring(expiryYear.length() - 2);
        paymentMethodUuid = UUID.randomUUID();

        paymentMethodsRepository.create_payment_method(
                paymentMethodUuid,
                customerUuid,
                encryptCardDetails(cardNumber),
                expiryMonth,
                expiryYear,
                encryptCardDetails(cvv),
                cardholderName,
                isDefault
        );

        logger.trace("Payment Method Added");

    }
}
