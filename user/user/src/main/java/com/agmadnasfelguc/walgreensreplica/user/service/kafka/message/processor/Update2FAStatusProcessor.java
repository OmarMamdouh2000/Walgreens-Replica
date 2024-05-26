package com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.processor;

import com.agmadnasfelguc.walgreensreplica.user.service.command.Update2FAStatusCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.keys.Keys;

import java.util.Map;

public class Update2FAStatusProcessor extends Processor {

    @Override
    public void process() {
        Update2FAStatusCommand typeCastedCommand = (Update2FAStatusCommand) getCommand();

        Map<String, String> paramsInfo = getMessageInfo().get(Keys.params);
        Map<String, String> messageInfo = getMessageInfo().get(Keys.body);

        typeCastedCommand.setSessionId(paramsInfo.get(Keys.sessionId));
        typeCastedCommand.setTwoFAEnabled(Boolean.parseBoolean(messageInfo.get(Keys.status)));

    }
}
