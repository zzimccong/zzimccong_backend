package com.project.zzimccong.controller.event;

import com.project.zzimccong.model.entity.coupon.Coupon;
import com.project.zzimccong.model.entity.event.Event;
import com.project.zzimccong.model.entity.event.EventParticipation;
import com.project.zzimccong.model.entity.user.User;
import com.project.zzimccong.repository.coupon.CouponRepository;
import com.project.zzimccong.repository.event.EventRepository;
import com.project.zzimccong.repository.user.UserRepository;
import com.project.zzimccong.service.event.EventParticipationService;
import com.project.zzimccong.service.notification.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/lottery-events")
public class EventParticipationController {

    private final EventParticipationService eventParticipationService;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;

    private final NotificationService notificationService;

    // Controller 생성자
    public EventParticipationController(EventParticipationService eventParticipationService,
                                        EventRepository eventRepository,
                                        UserRepository userRepository, CouponRepository couponRepository, NotificationService notificationService) {
        this.eventParticipationService = eventParticipationService;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.couponRepository = couponRepository;
        this.notificationService = notificationService;
    }

    // 이벤트 참여 요청 처리
    @PostMapping("/{eventId}/participate")
    public ResponseEntity<String> participateInEvent(
            @PathVariable Long eventId,
            @RequestBody Map<String, Integer> requestBody) {

        Integer userId = requestBody.get("userId");
        int couponCount = requestBody.getOrDefault("couponCount", 0);

        try {
            EventParticipation participation = eventParticipationService.participateInEvent(userId, eventId, couponCount);

            // 이벤트 참여 후 푸시 알림 전송
            String userToken = notificationService.getUserToken(userId);
            if (userToken != null) {
                String title = "이벤트 참여 완료!";
                String body = "이벤트에 성공적으로 참여했습니다. 추첨을 기대해 주세요!";
                notificationService.sendMessage(userToken, title, body);
            }

            return ResponseEntity.ok("참여 성공. 참여 ID: " + participation.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("푸시 알림 전송 중 오류가 발생했습니다.");
        }
    }


}
