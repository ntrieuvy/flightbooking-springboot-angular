package com.bm.travelcore.service;

import java.util.Set;

public interface RedisService {
    void save(String key, String value, long durationInMinutes);
    String get(String key);
    void delete(String key);
    boolean exists(String key);
    public long getTtl(String key);
    Set<String> getKeys(String redisKey);
}
