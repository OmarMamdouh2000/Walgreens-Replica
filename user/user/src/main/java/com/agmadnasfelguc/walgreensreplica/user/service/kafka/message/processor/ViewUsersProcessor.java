package com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.processor;

import com.agmadnasfelguc.walgreensreplica.user.service.command.ViewUsersCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.keys.Keys;

public class ViewUsersProcessor extends Processor {
    @Override
    public void process() {

        ViewUsersCommand typeCastedCommand = (ViewUsersCommand) getCommand();
        typeCastedCommand.setSessionId(getMessageInfo().get(Keys.params).get(Keys.sessionId));
    }
}
