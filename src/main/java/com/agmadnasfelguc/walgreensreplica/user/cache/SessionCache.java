package com.agmadnasfelguc.walgreensreplica.user.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.util.HashMap;

@Component
public class SessionCache {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private void storeSessionWithDetails(String sessionId, Map<String, String> sessionDetails, long timeout, TimeUnit unit) {
        sessionDetails.forEach((key, value) ->
                redisTemplate.opsForHash().put(sessionId, key, value));
        redisTemplate.expire(sessionId, timeout, unit);
    }

    public Map<String, String> getSessionDetails(String sessionId) {
        Map<Object, Object> rawMap = redisTemplate.opsForHash().entries(sessionId);
        Map<String, String> sessionDetails = new HashMap<>();
        for (Map.Entry<Object, Object> entry : rawMap.entrySet()) {
            sessionDetails.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
        return sessionDetails;
    }

    public void storeOtp(String email, OTPTypes otpType, String otpValue, long timeout, TimeUnit unit) {
        String otpKey = String.format("otp:%s:%s", email, otpType);
        redisTemplate.opsForValue().set(otpKey, otpValue, timeout, unit);
    }

    public String getOtp(String email, OTPTypes otpType) {
        String otpKey = String.format("otp:%s:%s", email, otpType);
        return redisTemplate.opsForValue().get(otpKey);
    }

    public void createSession(String sessionId, String userId, String role, String email, String firstName, String lastName) {
        HashMap<String, String> sessionDetails = new HashMap<>();
        sessionDetails.put("userId", userId);
        sessionDetails.put("role", role);
        sessionDetails.put("email", email);
        sessionDetails.put("firstName", firstName);
        sessionDetails.put("lastName", lastName);
        storeSessionWithDetails(sessionId, sessionDetails, 10, TimeUnit.HOURS);
    }

    public void createAdminSession(String sessionId, String userId, String username) {
        HashMap<String, String> sessionDetails = new HashMap<>();
        sessionDetails.put("userId", userId);
        sessionDetails.put("role", "admin");
        sessionDetails.put("username", username);
        storeSessionWithDetails(sessionId, sessionDetails, 10, TimeUnit.HOURS);
    }

    public void deleteSession(String sessionId) {
        redisTemplate.delete(sessionId);
    }

    public void deleteOtp(String email, OTPTypes otpType) {
        String otpKey = String.format("otp:%s:%s", email, otpType);
        redisTemplate.delete(otpKey);
    }
}


