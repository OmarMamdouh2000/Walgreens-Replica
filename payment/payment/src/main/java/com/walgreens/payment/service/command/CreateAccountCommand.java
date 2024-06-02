package com.walgreens.payment.service.command;

import com.walgreens.payment.repository.AccountRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountCommand implements Command{

    private  UUID userId;

    @Autowired
    private AccountRepository accountRepository;



    @Override
    public void execute() {
        UUID accountId = UUID.randomUUID();
      //  accountRepository.create_account(accountId, this.getUserId());

    }

}
