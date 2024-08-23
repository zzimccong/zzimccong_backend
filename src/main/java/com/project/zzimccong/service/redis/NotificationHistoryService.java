package com.project.zzimccong.service.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class NotificationHistoryService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final long EXPIRATION_TIME = 10; // 만료 시간 (일 단위로 설정)

    public NotificationHistoryService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 공통된 알림 기록 저장 메서드
    private void saveNotificationHistory(String key, String title, String message, String time) {
        String combinedMessage = "제목: " + title + "\n내용: " + message + "\n시간: " + time;
        redisTemplate.opsForList().leftPush(key, combinedMessage);
        redisTemplate.expire(key, EXPIRATION_TIME, TimeUnit.DAYS); // 만료 시간 설정
    }

    // 사용자 알림 기록 저장
    public void saveUserNotificationHistory(Integer userId, String title, String message, String time) {
        String key = "user:" + userId;
        saveNotificationHistory(key, title, message, time);
    }

    // 기업 알림 기록 저장
    public void saveCorpNotificationHistory(Integer corpId, String title, String message, String time) {
        String key = "corp:" + corpId;
        saveNotificationHistory(key, title, message, time);
    }

    // 공통된 알림 기록 조회 메서드
    private List<String> getNotificationHistory(String key) {
        List<Object> historyObjects = redisTemplate.opsForList().range(key, 0, -1);

        if (historyObjects == null) {
            return List.of();
        }

        return historyObjects.stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    // 사용자 알림 기록 조회
    public List<String> getUserNotificationHistory(Integer userId) {
        String key = "user:" + userId;
        return getNotificationHistory(key);
    }

    // 기업 알림 기록 조회
    public List<String> getCorpNotificationHistory(Integer corpId) {
        String key = "corp:" + corpId;
        return getNotificationHistory(key);
    }
}
