package com.walgreens.payment.service.kafka.message.processor;

import com.walgreens.payment.service.command.Command;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.*;
import java.util.Map.*;

@Getter
public abstract class Processor {
    private Command command;
    private JsonNode message;
    private Map<String, String> messageInfo = new HashMap<>();
    public abstract void process();

    public void init(Command command, JsonNode message) {
        this.command = command;
        this.message = message;
        loadMessageInfo();
    }

    public void loadMessageInfo() {
        if (message.isObject()) {
            Iterator<Entry<String, JsonNode>> fields = message.fields();
            while (fields.hasNext()) {
                Entry<String, JsonNode> field = fields.next();

                messageInfo.put(field.getKey(), field.getValue().asText());
            }
        } else {
            System.out.println("Provided JsonNode is not an ObjectNode");
        }



    }

    public Map<String, Map<String, String>> getNodes(List<String> nodeNames) {
        Map<String, Map<String, String>> result = new HashMap<>();

        for (String nodeName : nodeNames) {
            result.put(nodeName, getNode(nodeName));
        }

        return result;
    }

    private Map<String, String> getNode(String nodeName) {
        Map<String, String> map = new HashMap<>();
        JsonNode node = message.get(nodeName);
        if (node != null) {
            Iterator<Map.Entry<String, JsonNode>> it = node.fields();
            while (it.hasNext()) {
                Map.Entry<String, JsonNode> field = it.next();
                map.put(field.getKey(), field.getValue().asText());
            }
        }
        return map;
    }
}