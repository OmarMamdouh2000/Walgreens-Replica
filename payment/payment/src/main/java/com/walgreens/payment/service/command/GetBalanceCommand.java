package com.walgreens.payment.service.command;

import com.walgreens.payment.repository.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetBalanceCommand implements Command{

    private  UUID accountId;

    @Autowired
    private AccountRepository accountRepository;



  //  @Override
    public void execute() {
//        accountRepository.get_balance(accountId);
//
//
    }

}
