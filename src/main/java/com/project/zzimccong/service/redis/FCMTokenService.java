package com.project.zzimccong.service.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class FCMTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final long FCM_TOKEN_EXPIRATION_TIME = 30; // 토큰 만료 시간 (일 단위)

    public FCMTokenService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveFcmToken(String userId, String token) {
        redisTemplate.opsForValue().set(userId, token, FCM_TOKEN_EXPIRATION_TIME, TimeUnit.DAYS);
    }

    public String getFcmToken(String userId) {
        return redisTemplate.opsForValue().get(userId);
    }

    public void deleteFcmToken(String userId) {
        redisTemplate.delete(userId);
    }
}
