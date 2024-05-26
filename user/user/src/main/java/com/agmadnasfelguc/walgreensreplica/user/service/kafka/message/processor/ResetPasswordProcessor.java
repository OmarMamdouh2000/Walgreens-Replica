package com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.processor;

import com.agmadnasfelguc.walgreensreplica.user.service.command.ResetPasswordCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.keys.Keys;

import java.util.Map;

public class ResetPasswordProcessor extends Processor {

    @Override
    public void process() {

        ResetPasswordCommand typeCastedCommand = (ResetPasswordCommand) getCommand();
        Map<String, String> messageInfo = getMessageInfo().get(Keys.body);
        typeCastedCommand.setEmail(messageInfo.get(Keys.email));

    }
}
