package com.project.zzimccong.service.coupon;

import com.project.zzimccong.model.entity.coupon.Coupon;

public interface CouponService {
    Integer findCntByUserIdAndType(Integer userId, String type);
    Coupon findDiscountCouponByUserId(Integer userId);
}
