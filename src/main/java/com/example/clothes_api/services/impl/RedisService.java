package com.example.clothes_api.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void saveUserConnection(String email) {
        System.out.println("Save user successfully ");
        redisTemplate.opsForValue().set(email,"true");
    }

    public void deleteUserConnection(String email) {
        redisTemplate.delete(email);
    }

    public boolean isUserConnected(String email) {
        System.out.println(redisTemplate.hasKey(email));
        return Boolean.TRUE.equals(redisTemplate.hasKey(email));
    }

    public void saveOtp(String email, String otp) {
        redisTemplate.opsForValue().set(email+"OTP", otp,45, TimeUnit.SECONDS);
    }

    public boolean checkOtp(String email, String otp) {
        if(Boolean.TRUE.equals(redisTemplate.hasKey(email + "OTP"))){
            boolean result = redisTemplate.opsForValue().get(email+"OTP").equals(otp);
            if(result){
                redisTemplate.delete(email+"OTP");
            }
            return result;
        }
        return false;
    }
}
