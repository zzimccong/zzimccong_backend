package com.project.zzimccong.controller.notification;


import com.project.zzimccong.model.dto.notification.NotificationRequest;
import com.project.zzimccong.security.service.corp.CorpDetails;
import com.project.zzimccong.security.service.user.UserDetailsImpl;
import com.project.zzimccong.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController

public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/save-token")
    public ResponseEntity<String> saveToken(@RequestBody TokenRequest tokenRequest,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails instanceof UserDetailsImpl) {
            Integer userId = ((UserDetailsImpl) userDetails).getUser().getId();
            notificationService.saveUserToken(userId, tokenRequest.getToken());
        } else if (userDetails instanceof CorpDetails) {
            Integer corpId = ((CorpDetails) userDetails).getCorporation().getId();
            notificationService.saveCorpToken(corpId, tokenRequest.getToken());
        }
        return ResponseEntity.ok("토큰이 성공적으로 저장되었습니다.");
    }


    @PostMapping("/delete-token")
    public ResponseEntity<String> deleteToken(@RequestBody TokenRequest tokenRequest,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails instanceof UserDetailsImpl) {
            Integer userId = ((UserDetailsImpl) userDetails).getUser().getId();
            notificationService.deleteUserToken(userId); // 사용자 토큰 삭제
            log.info("사용자 ID: {}의 토큰이 삭제되었습니다.", userId);
        } else if (userDetails instanceof CorpDetails) {
            Integer corpId = ((CorpDetails) userDetails).getCorporation().getId();
            notificationService.deleteCorpToken(corpId); // 기업 토큰 삭제
            log.info("기업 ID: {}의 토큰이 삭제되었습니다.", corpId);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 사용자 정보입니다.");
        }
        return ResponseEntity.ok("토큰이 성공적으로 삭제되었습니다.");
    }

    @PostMapping("/send-notification/user")
    public ResponseEntity<String> sendUserNotification(@RequestBody NotificationRequest notificationRequest,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            log.warn("인증된 사용자 정보가 없습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인되지 않은 사용자입니다.");
        }

        Integer userId = userDetails.getUser().getId();

        try {
            notificationService.saveUserToken(userId, notificationRequest.getToken());

            // 알림 전송
            notificationService.sendMessage(notificationRequest.getToken(), notificationRequest.getTitle(), notificationRequest.getMessage());
            return ResponseEntity.ok("알림이 성공적으로 전송되었습니다.");
        } catch (Exception e) {
            log.error("알림 전송 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알림 전송 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @PostMapping("/send-notification/corp")
    public ResponseEntity<String> sendCorpNotification(@RequestBody NotificationRequest notificationRequest,
                                                       @AuthenticationPrincipal CorpDetails corpDetails) {
        if (corpDetails == null) {
            log.warn("인증된 기업 사용자 정보가 없습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자가 인증되지 않았습니다.");
        }

        Integer corpId = corpDetails.getCorporation().getId();

        try {
            notificationService.saveCorpToken(corpId, notificationRequest.getToken());
            notificationService.sendMessage(notificationRequest.getToken(), notificationRequest.getTitle(), notificationRequest.getMessage());
            return ResponseEntity.ok("알림이 성공적으로 전송되었습니다.");
        } catch (Exception e) {
            log.error("알림 전송 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알림 전송 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

}
