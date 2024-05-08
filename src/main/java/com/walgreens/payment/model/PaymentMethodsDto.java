package com.walgreens.payment.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "paymentmethods"
)
public class PaymentMethodsDto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID payment_method_uuid ;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_customer_uuid", referencedColumnName = "customer_uuid")
    private CustomerDto customer_uuid;

    private String card_number;
    private String expiry_month;
    private String expiry_year;
    private String cvv;
    private String cardholder_name;
    private Boolean has_funds;
    private Boolean is_default;

}
