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

    @Override
    public void deleteUserToken(Integer userId) {
        deleteToken("user:" + userId, "user");
        // 삭제 후 검증 (테스트용 코드)
        if (fcmTokenService.getFcmToken("user:" + userId) == null) {
            log.info("토큰이 성공적으로 삭제되었습니다.");
        } else {
            log.error("토큰 삭제 실패!");
        }
    }

    @Override
    public void deleteCorpToken(Integer corpId) {
        deleteToken("corp:" + corpId, "corp");
        // 삭제 후 검증 (테스트용 코드)
        if (fcmTokenService.getFcmToken("corp:" + corpId) == null) {
            log.info("토큰이 성공적으로 삭제되었습니다.");
        } else {
            log.error("토큰 삭제 실패!");
        }
    }

    private void deleteToken(String key, String entityType) {
        log.info("{} ID: {}에 대한 FCM 토큰을 Redis에서 삭제 요청 중...", entityType, key);
        fcmTokenService.deleteFcmToken(key);
        log.info("{} ID: {}의 FCM 토큰이 Redis에서 성공적으로 삭제되었습니다.", entityType, key);
    }
}