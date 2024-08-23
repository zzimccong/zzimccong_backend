package com.project.zzimccong.service.notification;


import com.project.zzimccong.service.redis.FCMTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {


    private final FCMTokenService fcmTokenService;

    @Override
    public void saveUserToken(Integer userId, String token) {
        log.info("사용자 ID: {}에 대한 토큰을 Redis에 저장 중...", userId);
        fcmTokenService.saveFcmToken("user:" + userId, token);
        log.info("사용자 ID: {}에 새로운 토큰이 Redis에 저장되었습니다.", userId);
    }

    @Override
    public void saveCorpToken(Integer corpId, String token) {
        log.info("기업 ID: {}에 대한 토큰을 Redis에 저장 중...", corpId);
        fcmTokenService.saveFcmToken("corp:" + corpId, token);
        log.info("기업 ID: {}에 새로운 토큰이 Redis에 저장되었습니다.", corpId);
    }

    @Override
    public String getUserToken(Integer userId) {
        log.info("Redis에서 사용자 ID: {}의 토큰을 조회 중...", userId);
        return fcmTokenService.getFcmToken("user:" + userId);
    }

    @Override
    public String getCorpToken(Integer corpId) {
        log.info("Redis에서 기업 ID: {}의 토큰을 조회 중...", corpId);
        return fcmTokenService.getFcmToken("corp:" + corpId);
    }
}