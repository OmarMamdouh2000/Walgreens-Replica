package com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.creator;

import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.keys.Keys;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class MessageCreator {
    private String templatePath;
    private Map<String,String> paramsMap = new HashMap<>();
    private Map<String,String> bodyMap = new HashMap<>();
    public JsonNode createMessage(){
        if(templatePath == null ||(paramsMap.isEmpty() && bodyMap.isEmpty())){
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(templatePath);
            // Load the JSON file into a JsonNode
            JsonNode root = objectMapper.readTree(inputStream);

            // Assuming the structure of the JSON is known and matches what has been described
            if (root instanceof ObjectNode rootNode) {
                JsonNode paramsNode = rootNode.path(Keys.params);
                if (paramsNode instanceof ObjectNode paramsObjectNode) {
                    for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                        paramsObjectNode.put(entry.getKey(), entry.getValue());
                    }
                }
                JsonNode bodyNode = rootNode.path(Keys.body);
                if (bodyNode instanceof ObjectNode bodyObjectNode) {
                    for (Map.Entry<String, String> entry : bodyMap.entrySet()) {
                        bodyObjectNode.put(entry.getKey(), entry.getValue());
                    }
                }
            }
            return root;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
