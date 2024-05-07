package com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.admin;

import com.agmadnasfelguc.walgreensreplica.user.service.command.admin.ViewUsersCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.Processor;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.keys.Keys;

public class ViewUsersProcessor extends Processor {
    @Override
    public void process() {
        ViewUsersCommand typeCastedCommand = (ViewUsersCommand) getCommand();
        typeCastedCommand.setSessionId(getMessageInfo().get(Keys.params).get(Keys.sessionId));
    }
}
