package com.walgreens.payment.service.invoker;

import com.fasterxml.jackson.databind.JsonNode;
import com.walgreens.payment.service.command.Command;
import com.walgreens.payment.service.kafka.message.processor.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class PaymentInvoker {


    private final CommandToReqMapper map;


    @Autowired
    private ApplicationContext applicationContext;

    public PaymentInvoker() {
        this.map = CommandToReqMapper.getInstance();
    }

    public void callCommand(JsonNode body){
        String request = body.get("request").asText();
        System.out.println("Request: " + request);
        String commandName = map.getCommandsMap().get(request);
        if(commandName == null){
            System.out.println("Command not found");
            return;
        }
        try {
//          Class<?> commandClass = applicationContext.getBean(commandName);
            Class<?> processorClass = Class.forName("com.walgreens.payment.service.kafka.message.processor." + request + "Processor");
            Processor processor = (Processor) processorClass.getDeclaredConstructor().newInstance();
            Command command = (Command) applicationContext.getBean(commandName);
            processor.init(command,body);
            processor.process();
            command.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
