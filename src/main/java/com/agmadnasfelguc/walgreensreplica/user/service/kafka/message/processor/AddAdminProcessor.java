package com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.processor;

import com.agmadnasfelguc.walgreensreplica.user.service.command.AddAdminCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.keys.Keys;

import java.util.Map;

public class AddAdminProcessor extends Processor {

    @Override
    public void process() {
        AddAdminCommand typeCastedCommand = (AddAdminCommand) getCommand();
        Map<String,String> params = getMessageInfo().get(Keys.params);
        Map<String, String> messageInfo = getMessageInfo().get(Keys.body);
        typeCastedCommand.setSessionId(params.get(Keys.sessionId));
        typeCastedCommand.setUsername(messageInfo.get(Keys.username));
        typeCastedCommand.setPassword(messageInfo.get(Keys.password));
    }
}
