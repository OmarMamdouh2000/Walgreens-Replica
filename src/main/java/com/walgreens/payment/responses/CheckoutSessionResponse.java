package com.walgreens.payment.responses;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutSessionResponse {

    private String sessionUrl;
    private String[] errors;

}
