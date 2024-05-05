package com.agmadnasfelguc.walgreensreplica.user.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;
import java.util.Map;

@Component
public class RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    // Store session with additional details
    public void storeSessionWithDetails(String sessionId, Map<String, String> sessionDetails, long timeout, TimeUnit unit) {
        sessionDetails.forEach((key, value) ->
                redisTemplate.opsForHash().put("session:" + sessionId, key, value));
        redisTemplate.expire("session:" + sessionId, timeout, unit);
    }

    // Retrieve session details
    public Map<Object, Object> getSessionDetails(String sessionId) {
        return redisTemplate.opsForHash().entries("session:" + sessionId);
    }

    // Store an OTP within a session
    public void storeOtp(String sessionId, String otpType, String otpValue, long timeout, TimeUnit unit) {
        redisTemplate.opsForHash().put("session:" + sessionId, otpType, otpValue);
        redisTemplate.expire("session:" + sessionId, timeout, unit);  // Update expiration time upon new OTP insertion
    }

    // Get a specific OTP from a session
    public String getOtp(String sessionId, String otpType) {
        return (String) redisTemplate.opsForHash().get("session:" + sessionId, otpType);
    }
}

