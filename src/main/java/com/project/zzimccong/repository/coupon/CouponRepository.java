package com.project.zzimccong.repository.coupon;

import com.project.zzimccong.model.entity.coupon.Coupon;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {

    @Query("SELECT c.cnt FROM Coupon c WHERE c.user.id = :userId AND c.type = :type")
    Integer findCntByUserIdAndType(@Param("userId") Integer userId, @Param("type") String type);

    @Query("SELECT c FROM Coupon c WHERE c.user.id = :userId AND c.type = '할인권'")
    Coupon findDiscountCouponByUserId(@Param("userId") Integer userId);
}
