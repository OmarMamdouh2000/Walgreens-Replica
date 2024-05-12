package com.agmadnasfelguc.walgreensreplica.user.service.Utils;

import com.agmadnasfelguc.walgreensreplica.user.service.invoker.CommandToReqMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Map;

public class JsonReader {

    public static Map readJsonFromResourcesAsMap(String path) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = CommandToReqMapper.class.getClassLoader().getResourceAsStream(path);
            Map jsonMap;
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found! " + path);
            } else {
                // Directly parse InputStream to Map
                jsonMap = objectMapper.readValue(inputStream, Map.class);

            }
            return jsonMap;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JsonNode readJsonFromResourcesAsJson(String path) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = CommandToReqMapper.class.getClassLoader().getResourceAsStream(path);
            JsonNode root;
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found! " + path);
            } else {
                root = objectMapper.readTree(inputStream);
            }

            return root;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
