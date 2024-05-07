package com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.user;

import com.agmadnasfelguc.walgreensreplica.user.service.command.user.ChangeEmailCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.Processor;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.keys.Keys;

import java.util.Map;

public class ChangeEmailProcessor extends Processor {

        @Override
        public void process() {
            ChangeEmailCommand typeCastedCommand = (ChangeEmailCommand) getCommand();
            Map<String,String> paramsInfo = getMessageInfo().get(Keys.params);
            typeCastedCommand.setSessionID(paramsInfo.get(Keys.sessionId));
            Map<String, String> messageInfo = getMessageInfo().get(Keys.body);
            typeCastedCommand.setPassword(messageInfo.get(Keys.password));
            typeCastedCommand.setEmail(messageInfo.get(Keys.newEmail));
        }
}
