package com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.admin;

import com.agmadnasfelguc.walgreensreplica.user.service.command.admin.UnbanAccountCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.Processor;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.keys.Keys;

public class UnbanAccountProcessor extends Processor {
    @Override
    public void process() {
        UnbanAccountCommand typeCastedCommand = (UnbanAccountCommand) getCommand();
        typeCastedCommand.setUserIdToUnban(getMessageInfo().get(Keys.body).get(Keys.userId));
        typeCastedCommand.setSessionId(getMessageInfo().get(Keys.params).get(Keys.sessionId));
    }
}
