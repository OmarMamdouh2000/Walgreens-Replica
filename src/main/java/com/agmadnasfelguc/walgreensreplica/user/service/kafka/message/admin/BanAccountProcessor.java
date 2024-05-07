package com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.admin;

import com.agmadnasfelguc.walgreensreplica.user.service.command.admin.BanAccountCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.Processor;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.keys.Keys;

public class BanAccountProcessor extends Processor {
    @Override
    public void process() {
        BanAccountCommand typeCastedCommand = (BanAccountCommand) getCommand();
        typeCastedCommand.setUserIdToBan(getMessageInfo().get(Keys.body).get(Keys.userId));
        typeCastedCommand.setSessionId(getMessageInfo().get(Keys.params).get(Keys.sessionId));
    }
}
