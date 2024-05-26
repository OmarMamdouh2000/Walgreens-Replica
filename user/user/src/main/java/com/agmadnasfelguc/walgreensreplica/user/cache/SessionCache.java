package com.agmadnasfelguc.walgreensreplica.user.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class SessionCache {

    @Autowired
    @Qualifier("sessionRedisTemplate")
    private RedisTemplate<String, Object> sessionRedisTemplate;

    @Autowired
    @Qualifier("otpRedisTemplate")
    private RedisTemplate<String, Object> otpRedisTemplate;


    public void storeOtp(String email, OTPTypes otpType, Object otpValue, long timeout, TimeUnit unit) {
        String otpKey = String.format("otp:%s:%s", email, otpType);
        otpRedisTemplate.opsForValue().set(otpKey, otpValue, timeout, unit);
        System.out.println("OTP Stored: " + otpValue);
    }

    public String getOtp(String email, OTPTypes otpType) {
        String otpKey = String.format("otp:%s:%s", email, otpType);
        return String.valueOf(otpRedisTemplate.opsForValue().get(otpKey));
    }

    public void deleteOtp(String email, OTPTypes otpType) {
        String otpKey = String.format("otp:%s:%s", email, otpType);
        otpRedisTemplate.delete(otpKey);
    }

    private void storeSessionWithDetails(String sessionId, Map<String, Object> sessionDetails, long timeout, TimeUnit unit) {
        sessionDetails.forEach((key, value) ->
                sessionRedisTemplate.opsForHash().put(sessionId, key, value));
        sessionRedisTemplate.expire(sessionId, timeout, unit);
    }

    public void updateSessionDetails(String sessionId, String section, Map<String, Object> sessionDetails) {
        Map<String,Object> oldSessionDetails = getSessionSection(sessionId, section);
        oldSessionDetails.putAll(sessionDetails);
        storeSessionSectionNoTimeout(sessionId, section, oldSessionDetails);
    }

    private void storeSessionSection(String sessionId, String section, Map<String, Object> data, long timeout, TimeUnit unit) {
        String sectionKey = buildSectionKey(sessionId, section);
        data.forEach((key, value) ->
                sessionRedisTemplate.opsForHash().put(sectionKey, key, value));
        sessionRedisTemplate.expire(sectionKey, timeout, unit);
    }

    private void storeSessionSectionNoTimeout(String sessionId, String section, Map<String, Object> data) {
        String sectionKey = buildSectionKey(sessionId, section);
        data.forEach((key, value) ->
                sessionRedisTemplate.opsForHash().put(sectionKey, key, value));

    }

    public Map<String, Object> getSessionSection(String sessionId, String section) {
        String sectionKey = buildSectionKey(sessionId, section);
        Map<Object, Object> rawMap = sessionRedisTemplate.opsForHash().entries(sectionKey);
        if(rawMap.isEmpty()){
            return null;
        }
        Map<String, Object> data = new HashMap<>();
        rawMap.forEach((key, value) ->
                data.put(String.valueOf(key), value));
        return data;
    }

    public void createSession(String sessionId, String section, Map<String, Object> detailsToBeAdded, long timeout, TimeUnit unit) {
        storeSessionSection(sessionId, section, detailsToBeAdded, timeout, unit);
    }

    public void deleteSessionSection(String sessionId, String section) {
        String sectionKey = buildSectionKey(sessionId, section);
        sessionRedisTemplate.delete(sectionKey);
    }

    public void deleteCompleteSession(String sessionId) {
        // This method assumes deletion of all sections related to the sessionId
        deleteSessionSection(sessionId, "user");
        deleteSessionSection(sessionId, "cart");
        sessionRedisTemplate.delete(sessionId);
        // Add more sections as necessary
    }


    public Map<String, Object> createAdminSession(String sessionId, String userId) {
        HashMap<String, Object> sessionDetails = new HashMap<>();
        sessionDetails.put("userId", sessionId);
        sessionDetails.put("role", "admin");
        storeSessionWithDetails(sessionId, sessionDetails, 30, TimeUnit.MINUTES);
        return null;
    }

    public Map<String, Object> getAdminSessionDetails(String sessionId){
        Map<Object, Object> rawMap = sessionRedisTemplate.opsForHash().entries(sessionId);
        Map<String, Object> data = new HashMap<>();
        rawMap.forEach((key, value) ->
                data.put(String.valueOf(key), value));
        return data;
    }

    // Helper method to construct a Redis key for a given session and section
    private String buildSectionKey(String sessionId, String section) {
        return String.format("%s:%s", sessionId, section); // Constructs a key like "sessionId:section"
    }

    public List<UUID> getKeysContainingUser() {
        List<UUID> keysWithUser = new ArrayList<>();
        ScanOptions options = ScanOptions.scanOptions()
                .match("*user*")
                .count(1000)
                .build();

        try (Cursor<Object> cursor = sessionRedisTemplate.opsForSet().scan("", options)) {
            while (cursor.hasNext()) {
                UUID key = UUID.fromString(String.valueOf(cursor.next()));
                keysWithUser.add(key);
            }
        } catch (Exception e) {
            System.err.println("Error retrieving keys from Redis: " + e.getMessage());
        }
        return keysWithUser;
    }

}
