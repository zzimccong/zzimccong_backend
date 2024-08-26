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
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final PasswordEncoder passwordEncoder;
    // Controller 생성자
    public EventParticipationController(EventParticipationService eventParticipationService,
                                        EventRepository eventRepository,
                                        UserRepository userRepository, CouponRepository couponRepository, NotificationService notificationService,PasswordEncoder passwordEncoder) {
        this.eventParticipationService = eventParticipationService;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.couponRepository = couponRepository;
        this.notificationService = notificationService;
        this.passwordEncoder = passwordEncoder;
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

    // 특정 이벤트의 모든 참여 기록 조회
    @GetMapping("/{eventId}/participations")
    public ResponseEntity<List<EventParticipation>> getParticipationsByEventId(@PathVariable Long eventId) {
        try {
            List<EventParticipation> participations = eventParticipationService.getParticipationsByEventId(eventId);
            return ResponseEntity.ok(participations);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    }

    // 특정 이벤트에서 사용된 쿠폰의 총 개수 조회
    @GetMapping("/{eventId}/coupons/count")
    public ResponseEntity<Integer> getCouponsUsedCount(@PathVariable Long eventId) {
        try {
            // 이벤트에서 사용된 모든 쿠폰을 가져와 총 개수를 계산
            List<EventParticipation> participations = eventParticipationService.getParticipationsByEventId(eventId);
            int totalCouponsUsed = participations.stream().mapToInt(EventParticipation::getUsedCouponCount).sum();
            return ResponseEntity.ok(totalCouponsUsed);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(0);
        }
    }

    // 특정 이벤트에 참가한 모든 사용자의 이름 조회
    @GetMapping("/{eventId}/participants/names")
    public ResponseEntity<List<String>> getParticipantNamesByEventId(@PathVariable Long eventId) {
        try {
            List<String> participantNames = eventParticipationService.getParticipantNamesByEventId(eventId);
            if (participantNames == null) {
                participantNames = new ArrayList<>(); // 참가자 이름이 null인 경우 빈 리스트로 설정
            }
            return ResponseEntity.ok(participantNames);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 이벤트 추첨 실행
    @PostMapping("/{eventId}/lottery/draw")
    public ResponseEntity<Map<String, Object>> drawLottery(@PathVariable Long eventId) {
        try {
            Map<String, Object> result = eventParticipationService.drawLottery(eventId);

            if (result.containsKey("error")) {
                return ResponseEntity.badRequest().body(result);
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "추첨 중 오류가 발생했습니다."));
        }
    }

    // 특정 이벤트에서 사용자가 사용한 쿠폰 수 조회
    @GetMapping("/{eventId}/users/{userId}/coupons/total-used")
    public ResponseEntity<Integer> getTotalCouponsUsedByUserInEvent(
            @PathVariable Integer userId,
            @PathVariable Long eventId) {
        Integer totalCouponsUsed = eventParticipationService.getTotalCouponsUsedByUserInEvent(userId, eventId);
        return ResponseEntity.ok(totalCouponsUsed);
    }

    // 모든 이벤트에서 사용자가 사용한 쿠폰 수 조회
    @GetMapping("/users/{userId}/coupons/total-used")
    public ResponseEntity<Integer> getTotalCouponsUsedByUserInAllEvents(
            @PathVariable Integer userId) {
        Integer totalCouponsUsed = eventParticipationService.getTotalCouponsUsedByUserInAllEvents(userId);
        return ResponseEntity.ok(totalCouponsUsed);
    }

    // 임의의 참여자 생성
    @PostMapping("/{eventId}/generate-random-participants")
    public ResponseEntity<String> generateRandomParticipants(@PathVariable Long eventId) {
        try {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 이벤트 ID입니다."));

            for (int i = 1; i <= 100; i++) {
                int userId = 1000 + i;
                int couponCount = 5;

                User user = userRepository.findById(userId)
                        .orElseGet(() -> createRandomUser(userId));

                giveUserLotteryCoupons(user, couponCount);

                eventParticipationService.participateInEvent(user.getId(), eventId, couponCount);
            }

            return ResponseEntity.ok("100명의 임의의 참여자가 생성되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("임의의 참여자 생성 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 임의의 사용자 생성
    private User createRandomUser(int userId) {
        Random random = new SecureRandom();

        User user = new User();
        user.setId(userId);
        user.setLoginId(generateRandomLoginId()); // 랜덤 로그인 ID 생성
        user.setName(generateRandomName()); // 랜덤 이름 생성
        user.setPassword(passwordEncoder.encode("1234")); // 비밀번호 암호화
        user.setEmail("user" + userId + "+" + random.nextInt(100000) + "@example.com");
        user.setBirth(generateRandomBirthday()); // 랜덤 생일 생성
        user.setPhone(generateRandomPhoneNumber()); // 랜덤 전화번호 생성
        user.setRole(generateRandomRole()); // 역할 설정

        return userRepository.save(user);
    }

    private String generateRandomLoginId() {
        return "user" + UUID.randomUUID().toString().substring(0, 8);
    }

    private String generateRandomName() {
        String[] firstNames = {"김", "장", "신", "정", "도"};
        String[] lastNames = {"민", "구", "동", "정", "이"};
        Random random = new SecureRandom();
        return firstNames[random.nextInt(firstNames.length)] + lastNames[random.nextInt(lastNames.length)];
    }

    private LocalDate generateRandomBirthday() {
        int startYear = 1970;
        int endYear = 2000;
        int dayOfYear = new Random().nextInt(365) + 1;
        int randomYear = new Random().nextInt(endYear - startYear + 1) + startYear;
        return LocalDate.ofYearDay(randomYear, dayOfYear);
    }

    private String generateRandomPhoneNumber() {
        Random random = new SecureRandom();
        return "010-" + (random.nextInt(9000) + 1000) + "-" + (random.nextInt(9000) + 1000);
    }

    private String generateRandomRole() {
        return "USER";
    }

    // 사용자에게 추첨권 부여
    private void giveUserLotteryCoupons(User user, int couponCount) {
        for (int i = 0; i < couponCount; i++) {
            Coupon coupon = new Coupon();
            coupon.setUser(user);
            coupon.setType("추첨권");
            coupon.setCnt(5);
            couponRepository.save(coupon);
        }
    }
}
