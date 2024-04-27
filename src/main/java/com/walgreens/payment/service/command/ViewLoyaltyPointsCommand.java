package com.walgreens.payment.service.command;

import com.walgreens.payment.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ViewLoyaltyPointsCommand implements Command {

    private UUID customerUuid;

    @Autowired
    private CustomerRepository customerRepository;


    @Override
    public void execute() {
        int loyaltyPoints = customerRepository.get_loyalty_points(customerUuid);
        System.out.println(loyaltyPoints);
    }
}
