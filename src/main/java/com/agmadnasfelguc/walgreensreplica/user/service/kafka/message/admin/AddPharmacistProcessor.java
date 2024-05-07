package com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.admin;

import com.agmadnasfelguc.walgreensreplica.user.service.command.admin.AddPharmacistCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.Processor;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.keys.Keys;

import java.util.Map;

public class AddPharmacistProcessor extends Processor {
    @Override
    public void process() {
        AddPharmacistCommand typeCastedCommand = (AddPharmacistCommand) getCommand();

        Map<String, String> paramsInfo = getMessageInfo().get(Keys.params);
        Map<String, String> messageInfo = getMessageInfo().get(Keys.body);

        typeCastedCommand.setSessionId(paramsInfo.get(Keys.sessionId));
        typeCastedCommand.setFirstName(messageInfo.get(Keys.firstName));
        typeCastedCommand.setLastName(messageInfo.get(Keys.lastName));
        typeCastedCommand.setEmail(messageInfo.get(Keys.email));
        typeCastedCommand.setPassword(messageInfo.get(Keys.password));

    }
}
