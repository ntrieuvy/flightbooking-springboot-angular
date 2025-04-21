package com.bm.travelcore.service.impl;

import com.bm.travelcore.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(String key, String value, long durationInMinutes) {
        redisTemplate.opsForValue().set(key, value, durationInMinutes, TimeUnit.MINUTES);
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public Set<String> getKeys(String redisKey) {
        return Set.of();
    }

    @Override
    public long getTtl(String key) {
        return redisTemplate.getExpire(key, TimeUnit.MINUTES);
    }
}
