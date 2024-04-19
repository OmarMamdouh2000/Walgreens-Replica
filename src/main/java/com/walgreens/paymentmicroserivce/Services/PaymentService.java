package com.walgreens.paymentmicroserivce.Services;

import com.walgreens.paymentmicroserivce.PaymentCommands.Command;
import org.springframework.stereotype.Component;

@Component
public class PaymentService {
    private final CommandMapper commandMapper;

    public PaymentService(CommandMapper commandMapper) {
        this.commandMapper = commandMapper;
    }

    public void invokeCommand(Request request) {
        Command command = commandMapper.getCommand(request.getType());
        command.execute();
    }
}
