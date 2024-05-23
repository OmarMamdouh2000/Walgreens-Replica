package com.walgreens.payment.service.command;

import com.walgreens.payment.repository.PaymentMethodsRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jasypt.util.text.AES256TextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private Boolean hasFunds;

    Logger logger= LoggerFactory.getLogger(AddPaymentMethodCommand.class);


    @Value("${encryption.secret.key}")
    private String encryptionPassword;

    @Autowired
    private PaymentMethodsRepository paymentMethodsRepository;


    private AES256TextEncryptor textEncryptor;

    @PostConstruct
    public void init() {
        textEncryptor = new AES256TextEncryptor();
        textEncryptor.setPassword(encryptionPassword);
    }

    public String encryptCardDetails(String cardNumber) {
        return textEncryptor.encrypt(cardNumber);
    }

    public String decryptCardDetails(String encryptedCardNumber) {
        return textEncryptor.decrypt(encryptedCardNumber);
    }


    @Override
    public void execute() {
        if(expiryYear.length()>2)
            expiryYear = expiryYear.substring(expiryYear.length() - 2);
        if(Integer.parseInt(expiryMonth)>12)
        {
            logger.error("Month invalid");
            return;
        }
        paymentMethodUuid = UUID.randomUUID();
        System.out.println(hasFunds);
        paymentMethodsRepository.create_payment_method(
                paymentMethodUuid,
                customerUuid,
                encryptCardDetails(cardNumber),
                expiryMonth,
                expiryYear,
                encryptCardDetails(cvv),
                cardholderName,
                isDefault,
                hasFunds
        );

        logger.trace("Payment Method Added");

    }
}
