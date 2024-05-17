package com.walgreens.payment.service.kafka.message.processor;

import com.walgreens.payment.service.command.Command;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Getter
public abstract class Processor {
    private Command command;
    private JsonNode message;
    private Map<String,Map<String, String>> messageInfo;
    public abstract void process();

    public void init(Command command, JsonNode message) {
        this.command = command;
        this.message = message;
        loadMessageInfo();
    }

    public void loadMessageInfo() {
        this.messageInfo =  getNodes(List.of("params","body"));

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