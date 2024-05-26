package com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.processor;

import com.agmadnasfelguc.walgreensreplica.user.service.command.LoginAdminCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.keys.Keys;

import java.util.Map;

public class AdminLoginProcessor extends Processor {

    @Override
    public void process() {
        LoginAdminCommand typeCastedCommand = (LoginAdminCommand) getCommand();
        Map<String, String> messageInfo = getMessageInfo().get(Keys.body);
        typeCastedCommand.setUsername(messageInfo.get(Keys.username));
        typeCastedCommand.setPassword(messageInfo.get(Keys.password));
    }
}
