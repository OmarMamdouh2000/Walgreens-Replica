package com.walgreens.payment.service.invoker;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json;
import lombok.Getter;
import net.minidev.json.writer.JsonReader;

import java.util.HashMap;
import java.util.*;
import java.io.*;


public class CommandToReqMapper {
    public static CommandToReqMapper commandToReqMapper;

    @Getter
    private final HashMap<String, String> commandsMap = new HashMap<>();


    private CommandToReqMapper() {
        init();
    }

    private void init() {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            InputStream inputStream = CommandToReqMapper.class.getResourceAsStream("/static/payment_command_map.json");

            // Check if the file was found
            if (inputStream == null) {
                System.out.println("File not found");
                return;
            }

            Map<String, Object> map = objectMapper.readValue(inputStream, Map.class);

            // Print the Map
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                commandsMap.put(entry.getKey(), entry.getValue().toString());
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public static synchronized CommandToReqMapper getInstance() {
        if(commandToReqMapper == null)
            commandToReqMapper = new CommandToReqMapper();

        return commandToReqMapper;
    }

        public static void main(String[] args) {
        CommandToReqMapper commandToReqMapper = CommandToReqMapper.getInstance();
        System.out.println(commandToReqMapper.getCommandsMap());
    }

}
