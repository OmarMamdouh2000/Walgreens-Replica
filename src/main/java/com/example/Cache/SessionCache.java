package com.example.Cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.util.HashMap;

@Component
public class SessionCache {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private void storeSessionWithDetails(String sessionId, Map<String, Object> sessionDetails, long timeout, TimeUnit unit) {
        sessionDetails.forEach((key, value) ->
                redisTemplate.opsForHash().put(sessionId, key, value));
        redisTemplate.expire(sessionId, timeout, unit);
    }   

    private void storeSessionSection(String sessionId, String section, Map<String, Object> data, long timeout, TimeUnit unit) {
        String sectionKey = buildSectionKey(sessionId, section); // Construct the full Redis key for the section
        data.forEach((key, value) ->
                redisTemplate.opsForHash().put(sectionKey, key, value));
        redisTemplate.expire(sectionKey, timeout, unit); // Apply expiration to the section key
    }

    public Map<String, Object> getSessionSection(String sessionId, String section) {
        String sectionKey = buildSectionKey(sessionId, section);
        Map<Object, Object> rawMap = redisTemplate.opsForHash().entries(sectionKey);
        Map<String, Object> data = new HashMap<>();
        rawMap.forEach((key, value) ->
                data.put(String.valueOf(key), value));
        return data;
    }

    public void createSession(String sessionId, String section, Map<String, Object> detailsToBeAdded) {
        // Store user details under "user" section
        storeSessionSection(sessionId, section, detailsToBeAdded, 10, TimeUnit.HOURS);
    }

    public void deleteSessionSection(String sessionId, String section) {
        String sectionKey = buildSectionKey(sessionId, section);
        redisTemplate.delete(sectionKey);
    }

    public void deleteCompleteSession(String sessionId) {
        // This method assumes deletion of all sections related to the sessionId
        deleteSessionSection(sessionId, "user");
        deleteSessionSection(sessionId, "cart");
        // Add more sections as necessary
    }

    public void createAdminSession(String sessionId, String userId, String username) {
        HashMap<String, Object> sessionDetails = new HashMap<>();
        sessionDetails.put("userId", userId);
        sessionDetails.put("role", "admin");
        sessionDetails.put("username", username);
        storeSessionWithDetails(sessionId, sessionDetails, 10, TimeUnit.HOURS);
    }

    // Helper method to construct a Redis key for a given session and section
    private String buildSectionKey(String sessionId, String section) {
        return String.format("%s:%s", sessionId, section); // Constructs a key like "sessionId:section"
    }

}