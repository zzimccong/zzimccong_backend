package com.project.zzimccong.service.coupon;

import com.project.zzimccong.model.entity.coupon.Coupon;
import com.project.zzimccong.repository.coupon.CouponDSLRepository;
import com.project.zzimccong.repository.coupon.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CouponServiceImpl implements CouponService{

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponDSLRepository couponDSLRepository;

    public CouponServiceImpl(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    public Integer findCntByUserIdAndType(Integer userId, String type) {
        return couponRepository.findCntByUserIdAndType(userId, type);
    }

    @Override
    public Coupon findDiscountCouponByUserId(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId is null");
        }

        Optional<Coupon> coupon = Optional.ofNullable(couponRepository.findDiscountCouponByUserId(userId));
        return coupon.orElse(null);
    }

    @Override
    public void decreaseCouponCnt(Integer userId) {
        couponDSLRepository.decreaseCntByUserId(userId);
    }
}
