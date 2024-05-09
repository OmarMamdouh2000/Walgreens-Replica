package com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.processor;

import com.agmadnasfelguc.walgreensreplica.user.service.command.EditDetailsCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.keys.Keys;

import java.text.SimpleDateFormat;
import java.util.Map;

public class EditDetailsProcessor extends Processor {
    @Override
    public void process() {
        EditDetailsCommand typeCastedCommand = (EditDetailsCommand) getCommand();
        Map<String, String> paramsInfo = getMessageInfo().get(Keys.params);
        Map<String, String> messageInfo = getMessageInfo().get(Keys.body);
        typeCastedCommand.setSessionID(paramsInfo.get(Keys.sessionId));
        if (messageInfo.get(Keys.dateOfBirth).equals("placeholder")){
            typeCastedCommand.setDateOfBirth(formatDate(messageInfo.get(Keys.dateOfBirth)));
        }
        if(messageInfo.get(Keys.gender).equals("placeholder")){
            typeCastedCommand.setPhoneNumber(messageInfo.get(Keys.gender));
        }
        if(messageInfo.get(Keys.address).equals("placeholder")){
            typeCastedCommand.setAddress(messageInfo.get(Keys.address));
        }
        if(messageInfo.get(Keys.phoneNumber).equals("placeholder")){
            typeCastedCommand.setPhoneNumber(messageInfo.get(Keys.phoneNumber));
        }
        if(messageInfo.get(Keys.extension).equals("placeholder")){
            typeCastedCommand.setPhoneNumber(messageInfo.get(Keys.extension));
        }



    }

    private java.sql.Date formatDate(String dateOfBirth){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            java.util.Date date = formatter.parse(dateOfBirth);
            return new java.sql.Date(date.getTime());
        } catch (Exception e) {
            System.out.println("Error parsing the date: " + e.getMessage());
        }
        return null;
    }
}
