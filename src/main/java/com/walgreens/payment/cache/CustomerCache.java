package com.walgreens.payment.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class CustomerCache {
    private static final String USER_CACHE_PREFIX = "user:";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // Cache the mapping of UUID and Stripe ID
    public void cacheUserIds(UUID uuid, String stripeId) {
        redisTemplate.opsForValue().set(USER_CACHE_PREFIX + uuid, stripeId, 10, TimeUnit.HOURS);
    }

    // Retrieve the Stripe ID from the cache using the UUID
    public String getStripeId(UUID uuid) {
        return redisTemplate.opsForValue().get(USER_CACHE_PREFIX + uuid);
    }

    // Remove the mapping from the cache
    public void removeUserFromCache(String uuid) {
        redisTemplate.delete(USER_CACHE_PREFIX + uuid);
    }
}
