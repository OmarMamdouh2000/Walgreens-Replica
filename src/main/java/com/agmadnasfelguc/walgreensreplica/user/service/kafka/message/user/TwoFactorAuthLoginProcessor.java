package com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.user;

import com.agmadnasfelguc.walgreensreplica.user.service.command.user.TwoFactorAuthLoginCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.Processor;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.keys.Keys;

import java.util.Map;

public class TwoFactorAuthLoginProcessor extends Processor {

    @Override
    public void process() {
        TwoFactorAuthLoginCommand typeCastedCommand = (TwoFactorAuthLoginCommand) getCommand();


        Map<String, String> paramsInfo = getMessageInfo().get(Keys.params);
        Map<String, String> messageInfo = getMessageInfo().get(Keys.body);

        typeCastedCommand.setUserId(paramsInfo.get(Keys.userId));
        typeCastedCommand.setEmail(messageInfo.get(Keys.email));
        typeCastedCommand.setOtp(messageInfo.get(Keys.otp));
        typeCastedCommand.setFirstName(messageInfo.get(Keys.firstName));
        typeCastedCommand.setLastName(messageInfo.get(Keys.lastName));
        typeCastedCommand.setRole(messageInfo.get(Keys.role));



    }
}
