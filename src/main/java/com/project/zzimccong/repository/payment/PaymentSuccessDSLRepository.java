package com.project.zzimccong.repository.payment;

import com.project.zzimccong.model.entity.coupon.Coupon;
import com.project.zzimccong.model.entity.payment.PaymentSuccess;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.project.zzimccong.model.entity.coupon.QCoupon.coupon;

@Repository
@RequiredArgsConstructor
@Transactional
public class PaymentSuccessDSLRepository {

    private final JPAQueryFactory queryFactory;

    public void AddCouponData(String orderName, Integer userId) {
        String[] parts = orderName.split(" ");
        String itemName = parts[0];
        String cntString = parts[1].replaceAll("[^0-9]", "");
        int cnt = Integer.parseInt(cntString);

        Coupon existingCoupon = queryFactory
                .selectFrom(coupon)
                .where(coupon.user.id.eq(userId)
                        .and(coupon.type.eq(itemName)))
                .fetchOne();

        if (existingCoupon != null) {
            existingCoupon.setCnt(existingCoupon.getCnt() + cnt);

        }


    }
}
