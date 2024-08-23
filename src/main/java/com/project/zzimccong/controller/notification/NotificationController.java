package com.project.zzimccong.controller.notification;


import com.project.zzimccong.security.service.corp.CorpDetails;
import com.project.zzimccong.security.service.user.UserDetailsImpl;
import com.project.zzimccong.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
