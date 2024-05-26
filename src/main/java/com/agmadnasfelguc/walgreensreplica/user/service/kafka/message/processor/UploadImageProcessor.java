package com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.processor;

import com.agmadnasfelguc.walgreensreplica.user.service.command.Update2FAStatusCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.UploadImageCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.keys.Keys;

import java.util.Map;

public class UploadImageProcessor extends Processor{
    private String sessionId;
    private String imageId;


    @Override
    public void process() {
        UploadImageCommand typeCastedCommand = (UploadImageCommand) getCommand();

        Map<String, String> paramsInfo = getMessageInfo().get(Keys.params);
        Map<String, String> messageInfo = getMessageInfo().get(Keys.body);

        typeCastedCommand.setSessionId(paramsInfo.get(Keys.sessionId));
        typeCastedCommand.setImageId(messageInfo.get(Keys.imageId));

    }
}
