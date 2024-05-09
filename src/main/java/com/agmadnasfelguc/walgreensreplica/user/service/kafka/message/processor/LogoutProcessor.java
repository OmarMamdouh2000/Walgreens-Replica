package com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.processor;

import com.agmadnasfelguc.walgreensreplica.user.service.command.LogoutCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.keys.Keys;

public class LogoutProcessor extends Processor {

    @Override
    public void process() {
        LogoutCommand typeCastedCommand = (LogoutCommand) getCommand();
        typeCastedCommand.setSessionId(getMessageInfo().get(Keys.params).get(Keys.sessionId));
    }
}
