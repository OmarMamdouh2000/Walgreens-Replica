package com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.user;


import com.agmadnasfelguc.walgreensreplica.user.service.command.user.LoginUserCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.Processor;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.keys.Keys;

import java.util.Map;

public class UserLoginProcessor extends Processor {

    @Override
    public void process() {
        LoginUserCommand typeCastedCommand = (LoginUserCommand) getCommand();
        Map<String, String> messageInfo = getMessageInfo().get(Keys.body);
        typeCastedCommand.setEmail(messageInfo.get(Keys.email));
        typeCastedCommand.setPassword(messageInfo.get(Keys.password));
    }
}
