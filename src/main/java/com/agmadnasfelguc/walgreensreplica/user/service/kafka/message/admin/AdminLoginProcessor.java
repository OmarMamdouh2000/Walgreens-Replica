package com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.admin;

import com.agmadnasfelguc.walgreensreplica.user.service.command.admin.LoginAdminCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.Processor;
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
