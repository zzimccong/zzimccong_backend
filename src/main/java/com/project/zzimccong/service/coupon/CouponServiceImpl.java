package com.project.zzimccong.service.coupon;

import com.project.zzimccong.repository.coupon.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class CouponServiceImpl implements CouponService{

    private CouponRepository couponRepository;

    public CouponServiceImpl(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    public Integer findCntByUserIdAndType(Integer userId, String type) {
        return couponRepository.findCntByUserIdAndType(userId, type);
    }
}
