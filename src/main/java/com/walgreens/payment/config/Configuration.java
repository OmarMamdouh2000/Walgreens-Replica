package com.walgreens.payment.config;


import jakarta.annotation.PostConstruct;
import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@Configurable
public class Configuration {



    @Value("${encryption.secret.key}")
    private String secretKey;

    @Bean
    public AES256TextEncryptor textEncryptor() {
        AES256TextEncryptor textEncryptor = new AES256TextEncryptor();
        textEncryptor.setPassword(secretKey); // Use a strong, securely stored key.
        return textEncryptor;
    }


}
