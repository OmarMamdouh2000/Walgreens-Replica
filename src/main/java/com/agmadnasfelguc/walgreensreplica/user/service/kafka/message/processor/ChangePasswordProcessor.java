package com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.processor;

import com.agmadnasfelguc.walgreensreplica.user.service.command.ChangePasswordCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.keys.Keys;

import java.util.Map;

public class ChangePasswordProcessor extends Processor {

    @Override
    public void process() {
        ChangePasswordCommand typeCastedCommand = (ChangePasswordCommand) getCommand();
        Map<String,String> paramsInfo = getMessageInfo().get(Keys.params);
        typeCastedCommand.setSessionID(paramsInfo.get(Keys.sessionId));
        Map<String, String> messageInfo = getMessageInfo().get(Keys.body);
        typeCastedCommand.setOldPassword(messageInfo.get(Keys.oldPassword));
        typeCastedCommand.setNewPassword(messageInfo.get(Keys.newPassword));
    }
}
