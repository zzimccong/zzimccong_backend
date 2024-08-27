package com.project.zzimccong.repository.coupon;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.project.zzimccong.model.entity.coupon.QCoupon.coupon;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CouponDSLRepository {

    private final JPAQueryFactory queryFactory;

    @Transactional
    public void decreaseCntByUserId(Integer userId) {
        long updatedReservationRows = queryFactory
                .update(coupon)
                .set(coupon.cnt, coupon.cnt.subtract(2))
                .where(coupon.user.id.eq(userId)
                        .and(coupon.type.eq("예약쿠폰")))
                .execute();

        log.debug("Updated reservation coupon rows: {}", updatedReservationRows);

        long updatedLotteryRows = queryFactory
                .update(coupon)
                .set(coupon.cnt, coupon.cnt.subtract(1))
                .where(coupon.user.id.eq(userId)
                        .and(coupon.type.eq("추첨권")))
                .execute();

        log.debug("Updated lottery coupon rows: {}", updatedLotteryRows);
    }

    @Transactional
    public void increaseCntByUserId(Integer userId) {
        long updatedRows = queryFactory
                .update(coupon)
                .set(coupon.cnt, coupon.cnt.add(2))
                .where(coupon.user.id.eq(userId)
                        .and(coupon.type.eq("예약쿠폰")))
                .execute();

        System.out.println("Updated rows: " + updatedRows);
    }

}
