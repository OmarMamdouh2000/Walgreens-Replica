package com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.processor;

import com.agmadnasfelguc.walgreensreplica.user.service.command.ViewUserCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.keys.Keys;

import java.util.Map;

public class ViewUserProcessor extends Processor{

    @Override
    public void process() {
        Map<String,String> paramsInfo = getMessageInfo().get(Keys.params);
        ViewUserCommand typeCastedCommand = (ViewUserCommand) getCommand();
        typeCastedCommand.setSessionId(paramsInfo.get(Keys.sessionId));
    }
}
