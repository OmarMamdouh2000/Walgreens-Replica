package com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.processor;

import com.agmadnasfelguc.walgreensreplica.user.service.command.VerifyMailCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.keys.Keys;

public class VerifyMailProcessor extends Processor {
    @Override
    public void process() {
        VerifyMailCommand typeCastedCommand = (VerifyMailCommand) getCommand();
        typeCastedCommand.setSessionId(getMessageInfo().get(Keys.params).get(Keys.sessionId));
    }
}
