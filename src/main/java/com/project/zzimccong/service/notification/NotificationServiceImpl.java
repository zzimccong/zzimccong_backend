package com.project.zzimccong.service.notification;

import com.google.firebase.messaging.FirebaseMessaging;
import com.project.zzimccong.security.service.corp.CorpDetails;
import com.project.zzimccong.security.service.user.UserDetailsImpl;
import com.project.zzimccong.service.fcm.FcmMessage;
import com.project.zzimccong.service.redis.FCMTokenService;
import com.project.zzimccong.service.redis.NotificationHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {


    private final FirebaseMessaging firebaseMessaging;
    private final FCMTokenService fcmTokenService;
    private final NotificationHistoryService notificationHistoryService;

    @Override
    public void saveUserToken(Integer userId, String token) {
        try {
            log.info("사용자 ID: {}에 대한 토큰을 Redis에 저장 중...", userId);
            fcmTokenService.saveFcmToken("user:" + userId, token);
            log.info("사용자 ID: {}에 새로운 토큰이 Redis에 저장되었습니다.", userId);
        } catch (Exception e) {
            log.error("사용자 ID: {}에 대한 토큰 저장 중 오류 발생: {}", userId, e.getMessage());
        }
    }

    @Override
    public void saveCorpToken(Integer corpId, String token) {
        try {
        log.info("기업 ID: {}에 대한 토큰을 Redis에 저장 중...", corpId);
        fcmTokenService.saveFcmToken("corp:" + corpId, token);
        log.info("기업 ID: {}에 새로운 토큰이 Redis에 저장되었습니다.", corpId);
        } catch (Exception e) {
            log.error("사용자 ID: {}에 대한 토큰 저장 중 오류 발생: {}", corpId, e.getMessage());
        }

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

    @Override
    public void sendMessage(String token, String title, String message) throws Exception {
        log.info("토큰: {}으로 메시지 전송 중...", token);
        if (isUserLoggedIn()) {
            firebaseMessaging.send(FcmMessage.makeMessage(token, title, message));
            log.info("토큰: {}으로 메시지가 전송되었습니다.", token);
            saveNotificationHistory(title, message);
        } else {
            log.info("사용자가 로그인되어 있지 않아 메시지를 전송하지 않았습니다.");
        }
    }

    @Override
    public void sendMessages(List<String> tokens, String title, String message) throws Exception {
        log.info("여러 토큰으로 메시지 전송 중...");
        if (isUserLoggedIn()) {
            firebaseMessaging.sendMulticast(FcmMessage.makeMessages(tokens, title, message));
            log.info("여러 토큰으로 메시지가 전송되었습니다.");
        } else {
            log.info("사용자가 로그인되어 있지 않아 메시지를 전송하지 않았습니다.");
        }
    }

    private boolean isUserLoggedIn() {
        return SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof String);
    }

    private void saveNotificationHistory(String title, String message) {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            notificationHistoryService.saveUserNotificationHistory(userDetails.getUser().getId(), title, message, currentTime);
        } else if (authentication.getPrincipal() instanceof CorpDetails) {
            CorpDetails corpDetails = (CorpDetails) authentication.getPrincipal();
            notificationHistoryService.saveCorpNotificationHistory(corpDetails.getCorporation().getId(), title, message, currentTime);
        }
    }

    @Override
    public List<String> getUserNotificationHistory(Integer userId) {
        log.info("사용자 ID {}의 알림 기록을 조회합니다.", userId);
        return notificationHistoryService.getUserNotificationHistory(userId);
    }

    @Override
    public List<String> getCorpNotificationHistory(Integer corpId) {
        log.info("기업 ID {}의 알림 기록을 조회합니다.", corpId);
        return notificationHistoryService.getCorpNotificationHistory(corpId);
    }
}