package com.project.zzimccong.repository.coupon;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.project.zzimccong.model.entity.coupon.QCoupon.coupon;

@Repository
@RequiredArgsConstructor
public class CouponDSLRepository {

    private final JPAQueryFactory queryFactory;

    @Transactional
    public void decreaseCntByUserId(Integer userId) {
        long updatedRows = queryFactory
                .update(coupon)
                .set(coupon.cnt, coupon.cnt.subtract(2))
                .where(coupon.user.id.eq(userId)
                        .and(coupon.type.eq("예약쿠폰")))

                .set(coupon.cnt, coupon.cnt.subtract(1))
                .where(coupon.user.id.eq(userId)
                        .and(coupon.type.eq("추첨권")))
                .execute();

        System.out.println("Updated rows: " + updatedRows);
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
