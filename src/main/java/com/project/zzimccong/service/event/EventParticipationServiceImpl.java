package com.project.zzimccong.service.event;

import com.project.zzimccong.model.entity.coupon.Coupon;
import com.project.zzimccong.model.entity.event.Event;
import com.project.zzimccong.model.entity.event.EventParticipation;
import com.project.zzimccong.model.entity.user.User;
import com.project.zzimccong.repository.coupon.CouponRepository;
import com.project.zzimccong.repository.event.EventParticipationRepository;
import com.project.zzimccong.repository.event.EventRepository;
import com.project.zzimccong.repository.user.UserRepository;
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

    // Service 구현체 생성자
    public EventParticipationServiceImpl(CouponRepository couponRepository, UserRepository userRepository,
                                         EventRepository eventRepository,
                                         EventParticipationRepository eventParticipationRepository) {
        this.couponRepository = couponRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.eventParticipationRepository = eventParticipationRepository;
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
                .filter(coupon -> coupon.getCnt() > 0 && !coupon.getUsed())
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

            if (coupon.getCnt() == 0) {
                coupon.setUsed(true);
            }
            couponRepository.save(coupon);
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

        return savedParticipation;
    }


}
