package com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.processor;

import com.agmadnasfelguc.walgreensreplica.user.service.command.RegisterCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.keys.Keys;

import java.util.Map;

public class RegisterProcessor extends Processor {

    @Override
    public void process() {
        RegisterCommand typeCastedCommand = (RegisterCommand) getCommand();
        Map<String, String> messageInfo = getMessageInfo().get(Keys.body);
        typeCastedCommand.setEmail(messageInfo.get(Keys.email));
        typeCastedCommand.setPassword(messageInfo.get(Keys.password));
        typeCastedCommand.setFirstName(messageInfo.get(Keys.firstName));
        typeCastedCommand.setLastName(messageInfo.get(Keys.lastName));
    }
}
