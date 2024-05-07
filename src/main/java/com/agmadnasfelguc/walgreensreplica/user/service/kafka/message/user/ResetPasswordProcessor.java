package com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.user;

import com.agmadnasfelguc.walgreensreplica.user.service.command.user.ResetPasswordCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.Processor;
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
