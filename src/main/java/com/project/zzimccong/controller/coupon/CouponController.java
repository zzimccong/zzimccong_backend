package com.project.zzimccong.controller.coupon;

import com.project.zzimccong.service.coupon.CouponService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    private CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    // 사용자의 '예약권' 쿠폰 개수 조회
    @GetMapping("/{userId}/reservation/cnt")
    public Integer getReservationCouponsCnt(@PathVariable Integer userId) {
        return couponService.findCntByUserIdAndType(userId, "예약권");
    }

    // 사용자의 '추첨권' 쿠폰 개수 조회
    @GetMapping("/{userId}/lottery/cnt")
    public Integer getLotteryCouponsCnt(@PathVariable Integer userId) {
        return couponService.findCntByUserIdAndType(userId, "추첨권");
    }

}
