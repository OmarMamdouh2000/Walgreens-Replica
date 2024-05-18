package com.agmadnasfelguc.walgreensreplica.user.cache;

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

    public void updateSessionDetails(String sessionId, String section, Map<String, Object> sessionDetails) {
        Map<String,Object> oldSessionDetails = getSessionSection(sessionId, section);
        oldSessionDetails.putAll(sessionDetails);
        storeSessionSectionNoTimeout(sessionId, section, oldSessionDetails);
    }

    private void storeSessionSection(String sessionId, String section, Map<String, Object> data, long timeout, TimeUnit unit) {
        String sectionKey = buildSectionKey(sessionId, section);
        data.forEach((key, value) ->
                redisTemplate.opsForHash().put(sectionKey, key, value));
        redisTemplate.expire(sectionKey, timeout, unit);
    }

    private void storeSessionSectionNoTimeout(String sessionId, String section, Map<String, Object> data) {
        String sectionKey = buildSectionKey(sessionId, section);
        data.forEach((key, value) ->
                redisTemplate.opsForHash().put(sectionKey, key, value));

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
        redisTemplate.delete(sessionId);
        // Add more sections as necessary
    }

    public void storeOtp(String email, OTPTypes otpType, Object otpValue, long timeout, TimeUnit unit) {
        String otpKey = String.format("otp:%s:%s", email, otpType);
        redisTemplate.opsForValue().set(otpKey, otpValue, timeout, unit);
    }

    public String getOtp(String email, OTPTypes otpType) {
        String otpKey = String.format("otp:%s:%s", email, otpType);
        return String.valueOf(redisTemplate.opsForValue().get(otpKey));
    }

    public void createAdminSession(String sessionId, String userId, String username) {
        HashMap<String, Object> sessionDetails = new HashMap<>();
        sessionDetails.put("userId", userId);
        sessionDetails.put("role", "admin");
        sessionDetails.put("username", username);
        storeSessionWithDetails(sessionId, sessionDetails, 10, TimeUnit.HOURS);
    }

    public Map<String, Object> getAdminSessionDetails(String sessionId){
        Map<Object, Object> rawMap = redisTemplate.opsForHash().entries(sessionId);
        Map<String, Object> data = new HashMap<>();
        rawMap.forEach((key, value) ->
                data.put(String.valueOf(key), value));
        return data;
    }

    // Helper method to construct a Redis key for a given session and section
    private String buildSectionKey(String sessionId, String section) {
        return String.format("%s:%s", sessionId, section); // Constructs a key like "sessionId:section"
    }

    public void deleteOtp(String email, OTPTypes otpType) {
        String otpKey = String.format("otp:%s:%s", email, otpType);
        redisTemplate.delete(otpKey);
    }
}
