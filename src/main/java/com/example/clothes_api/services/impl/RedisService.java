package com.example.clothes_api.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void saveUserConnection(String email) {
        redisTemplate.opsForValue().set(email,false);
    }

    public void deleteUserConnection(String email) {
        redisTemplate.delete(email);
    }

    public boolean isUserConnected(String email) {
        return redisTemplate.hasKey(email);
    }
}
