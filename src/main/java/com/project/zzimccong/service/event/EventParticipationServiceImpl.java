package com.project.zzimccong.service.event;

import com.project.zzimccong.model.entity.coupon.Coupon;
import com.project.zzimccong.model.entity.event.Event;
import com.project.zzimccong.model.entity.event.EventParticipation;
import com.project.zzimccong.model.entity.user.User;
import com.project.zzimccong.repository.coupon.CouponRepository;
import com.project.zzimccong.repository.event.EventParticipationRepository;
import com.project.zzimccong.repository.event.EventRepository;
import com.project.zzimccong.repository.user.UserRepository;
import com.project.zzimccong.service.notification.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventParticipationServiceImpl implements EventParticipationService {

    private static final Logger logger = LoggerFactory.getLogger(EventParticipationServiceImpl.class);

    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventParticipationRepository eventParticipationRepository;
    private final NotificationService notificationService;

    // Service 구현체 생성자
    public EventParticipationServiceImpl(CouponRepository couponRepository,
                                         UserRepository userRepository,
                                         EventRepository eventRepository,
                                         EventParticipationRepository eventParticipationRepository,
                                         NotificationService notificationService) {
        this.couponRepository = couponRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.eventParticipationRepository = eventParticipationRepository;
        this.notificationService = notificationService;
    }


    @Override
    @Transactional
    public EventParticipation participateInEvent(Integer userId, Long eventId, int couponCount) {
        if (userId == null) {
            throw new IllegalArgumentException("사용자 ID는 null일 수 없습니다.");
        }
        if (eventId == null) {
            throw new IllegalArgumentException("이벤트 ID는 null일 수 없습니다.");
        }
        if (couponCount < 1) {
            throw new IllegalArgumentException("쿠폰 수량은 1개 이상이어야 합니다.");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 이벤트 ID입니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 ID입니다."));

        // 사용 가능한 추첨권 조회 및 차감
        List<Coupon> validCoupons = couponRepository.findAllByUserIdAndType(userId, "추첨권").stream()
                .filter(coupon -> coupon.getCnt() > 0)
                .collect(Collectors.toList());

        int totalAvailableCoupons = validCoupons.stream().mapToInt(Coupon::getCnt).sum();
        logger.debug("User ID: {}, Total Available Coupons: {}", userId, totalAvailableCoupons);

        if (totalAvailableCoupons < couponCount) {
            throw new IllegalArgumentException("추첨권이 부족합니다. 사용 가능한 수량: " + totalAvailableCoupons);
        }

        int remainingCouponCount = couponCount;
        for (Coupon coupon : validCoupons) {
            if (remainingCouponCount <= 0) break;

            int usedCount = Math.min(coupon.getCnt(), remainingCouponCount);
            coupon.setCnt(coupon.getCnt() - usedCount);
            remainingCouponCount -= usedCount;

            logger.debug("Coupon ID: {}, Used: {}, Remaining: {}", coupon.getId(), usedCount, coupon.getCnt());

            // 쿠폰 상태 업데이트
            updateCouponStatus(coupon);
        }

        EventParticipation participation = new EventParticipation(user, event, validCoupons.get(0), false, couponCount);
        EventParticipation savedParticipation = eventParticipationRepository.save(participation);

        logger.info("사용자 {}가 이벤트 {}에 참여하였습니다. 참여 ID: {}, 사용된 쿠폰 수량: {}", userId, eventId, savedParticipation.getId(), couponCount);

        // 전체 이벤트에서 사용된 총 쿠폰 수 업데이트
        int totalCouponsUsed = eventParticipationRepository.findTotalUsedCouponsByUserInAllEvents(userId);
        int newTotalCouponsUsed = totalCouponsUsed + couponCount;

        // 20장 단위로 할인권 발급
        int previousDiscounts = totalCouponsUsed / 20;
        int newDiscounts = newTotalCouponsUsed / 20;
        int discountsToIssue = newDiscounts - previousDiscounts;

        if (discountsToIssue > 0) {
            for (int i = 0; i < discountsToIssue; i++) {
                Coupon discountCoupon = new Coupon();
                discountCoupon.setUser(user);
                discountCoupon.setType("할인권");
                discountCoupon.setCnt(1);
                discountCoupon.setDiscountPrice(8000);
                discountCoupon.setUsed(false);
                couponRepository.save(discountCoupon);

                logger.info("사용자 {}에게 할인권 1장이 발급되었습니다.", userId);
            }
        }

        // 이벤트 참여 후 푸시 알림 전송
        String userToken = notificationService.getUserToken(userId);
        if (userToken != null) {
            String title = "이벤트 참여 완료!";
            String body = "이벤트에 성공적으로 참여했습니다. 추첨을 기대해 주세요!";
            try {
                notificationService.sendMessage(userToken, title, body);
            } catch (Exception e) {
                logger.error("사용자 {}에게 알림 전송 중 오류 발생: {}", userId, e.getMessage());
            }
        } else {
            logger.warn("사용자 {}의 알림 토큰을 찾을 수 없습니다.", userId);
        }

        return savedParticipation;
    }

    // 쿠폰 상태 업데이트 메서드
    public void updateCouponStatus(Coupon coupon) {
        if (coupon.getCnt() > 0) {
            coupon.setUsed(false);  // 수량이 0보다 크면 사용 가능한 상태로 변경
        } else if (coupon.getCnt() == 0) {
            coupon.setUsed(true);  // 수량이 0이면 사용된 상태로 설정
        }
        couponRepository.save(coupon);
    }

    @Override
    public List<EventParticipation> getParticipationsByEventId(Long eventId) {
        if (eventId == null) {
            throw new IllegalArgumentException("이벤트 ID는 null일 수 없습니다."); // 이벤트 ID 유효성 검사
        }
        return eventParticipationRepository.findByEventId(eventId); // 이벤트 ID로 참여 기록 조회
    }

    @Override
    public List<EventParticipation> getParticipationsByUserIdAndEventId(Integer userId, Long eventId) {
        if (userId == null) {
            throw new IllegalArgumentException("사용자 ID는 null일 수 없습니다."); // 사용자 ID 유효성 검사
        }
        if (eventId == null) {
            throw new IllegalArgumentException("이벤트 ID는 null일 수 없습니다."); // 이벤트 ID 유효성 검사
        }
        return eventParticipationRepository.findByUserIdAndEventId(userId, eventId); // 사용자와 이벤트 ID로 참여 기록 조회
    }

    @Override
    public List<String> getParticipantNamesByEventId(Long eventId) {
        if (eventId == null) {
            throw new IllegalArgumentException("이벤트 ID는 null일 수 없습니다."); // 이벤트 ID 유효성 검사
        }

        List<EventParticipation> participations = eventParticipationRepository.findByEventId(eventId); // 이벤트 ID로 참여 기록 조회
        return participations.stream()
                .map(participation -> participation.getUser().getName()) // 참여자의 이름을 리스트로 반환
                .collect(Collectors.toList());
    }

    @Override
    public List<Coupon> getCouponsUsedInEvent(Long eventId) {
        if (eventId == null) {
            throw new IllegalArgumentException("이벤트 ID는 null일 수 없습니다."); // 이벤트 ID 유효성 검사
        }
        return eventParticipationRepository.findCouponsByEventId(eventId); // 이벤트에서 사용된 쿠폰 조회
    }



    @Override
    @Transactional
    public Map<String, Object> drawLottery(Long eventId) {
        if (eventId == null) {
            throw new IllegalArgumentException("이벤트 ID는 null일 수 없습니다.");
        }

        try {
            List<EventParticipation> participations = eventParticipationRepository.findByEventId(eventId);

            if (participations.isEmpty()) {
                return Collections.singletonMap("error", "이 이벤트에 참여한 사람이 없습니다.");
            }

            Random random = new Random();
            int winnerIndex = random.nextInt(participations.size());
            EventParticipation winner = participations.get(winnerIndex);

            Map<String, Object> response = new HashMap<>();
            response.put("winnerName", winner.getUser().getName());
            response.put("winnerIndex", winnerIndex);

            winner.setWinner(true);
            eventParticipationRepository.save(winner);

            // 알림 전송
            String winnerMessage = "축하합니다! " + winner.getUser().getName() + " 님이 당첨되었습니다!";
            String resultLink = "http://localhost:3000/event-participation" + eventId ;  // 결과를 볼 수 있는 링크
            String messageToAll = "추첨이 완료되었습니다. 이벤트 추첨 결과 보기: " + resultLink;

            sendNotificationToAllParticipants(participations, "이벤트 추첨 결과", winnerMessage, messageToAll);

            return response;
        } catch (Exception e) {
            logger.error("추첨 중 오류 발생", e);
            return Collections.singletonMap("error", "추첨 중 오류가 발생했습니다.");
        }
    }

    private void sendNotificationToAllParticipants(List<EventParticipation> participations, String title, String winnerMessage, String messageToAll) {
        for (EventParticipation participation : participations) {
            Integer userId = participation.getUser().getId();
            String userToken = notificationService.getUserToken(userId);
            if (userToken != null) {
                try {
                    logger.info("Redis에서 사용자 ID: {}의 토큰 조회 성공: {}", userId, userToken);
                    // 당첨자에게는 당첨 메시지를 보냄
                    if (participation.getWinner()) {

                        notificationService.sendMessage(userToken, title, winnerMessage);
                    } else {
                        // 다른 모든 참가자에게는 일반 추첨 완료 메시지를 보냄

                        notificationService.sendMessage(userToken, title, messageToAll);
                    }
                } catch (Exception e) {
                    logger.warn("Redis에서 사용자 ID: {}의 토큰을 찾지 못했습니다.", userId);
                    logger.error("사용자 ID: {}에게 알림 전송 중 오류가 발생했습니다: {}", userId, e.getMessage());
                }
            }
        }
    }

    @Override
    public Integer getTotalCouponsUsedByUserInEvent(Integer userId, Long eventId) {
        if (userId == null || eventId == null) {
            throw new IllegalArgumentException("사용자 ID와 이벤트 ID는 null일 수 없습니다."); // 사용자 ID와 이벤트 ID 유효성 검사
        }

        Integer totalUsed = eventParticipationRepository.findTotalUsedCouponsByUserInEvent(userId, eventId); // 특정 이벤트에서 사용한 쿠폰 수 조회

        if (totalUsed == null) {
            totalUsed = 0; // 쿠폰 사용 내역이 없을 경우 0 반환
        }

        logger.info("User {} used {} coupons in event {}", userId, totalUsed, eventId); // 로그 기록

        return totalUsed; // 총 사용된 쿠폰 수 반환
    }

    @Override
    public Integer getTotalCouponsUsedByUserInAllEvents(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("사용자 ID는 null일 수 없습니다."); // 사용자 ID 유효성 검사
        }

        Integer totalCouponsUsed = eventParticipationRepository.findTotalUsedCouponsByUserInAllEvents(userId); // 모든 이벤트에서 사용한 쿠폰 수 조회

        if (totalCouponsUsed == null) {
            totalCouponsUsed = 0; // 쿠폰 사용 내역이 없을 경우 0 반환
        }

        logger.info("User {} has used {} coupons across all events", userId, totalCouponsUsed); // 로그 기록

        return totalCouponsUsed; // 총 사용된 쿠폰 수 반환
    }
}
