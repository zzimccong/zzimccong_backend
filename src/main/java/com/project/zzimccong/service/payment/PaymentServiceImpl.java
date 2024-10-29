package com.project.zzimccong.service.payment;

import com.project.zzimccong.model.dto.payment.PaymentSuccessDTO;
import com.project.zzimccong.model.entity.coupon.Coupon;
import com.project.zzimccong.model.entity.payment.Payment;
import com.project.zzimccong.model.entity.payment.PaymentSuccess;
import com.project.zzimccong.model.entity.user.User;
import com.project.zzimccong.repository.coupon.CouponRepository;
import com.project.zzimccong.repository.payment.PaymentRepository;
import com.project.zzimccong.repository.payment.PaymentSuccessDSLRepository;
import com.project.zzimccong.repository.payment.PaymentSuccessRepository;
import com.project.zzimccong.service.user.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {


    private final PaymentRepository paymentRepository;
    private final UserService userService; // UserService 주입
    private final PaymentSuccessRepository paymentSuccessRepository;
    private final PaymentSuccessDSLRepository paymentSuccessDSLRepository;

    private final CouponRepository couponRepository;
    public PaymentServiceImpl(PaymentRepository paymentRepository, UserService userService, PaymentSuccessRepository paymentSuccessRepository, PaymentSuccessDSLRepository paymentSuccessDSLRepository, CouponRepository couponRepository) {
        this.paymentRepository = paymentRepository;
        this.userService = userService;
        this.paymentSuccessRepository = paymentSuccessRepository;
        this.paymentSuccessDSLRepository = paymentSuccessDSLRepository;
        this.couponRepository = couponRepository;
    }

    @Override
    public Payment requestPayment(Payment payment, Integer userId) {
        User user = userService.findById(userId);
        if (user != null) {
            payment.setCustomer(user);
            return paymentRepository.save(payment);
        } else {
            throw new IllegalArgumentException("User not found with Id: " + userId);
        }
    }

    @Override
    public int AddSuccessData(String orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId);

        if (payment != null && !paymentSuccessRepository.existsByOrderId(orderId)) {
            PaymentSuccess paymentSuccess = PaymentSuccess.builder()
                    .paymentId(payment.getPaymentId())
                    .payType(payment.getPayType())
                    .amount(payment.getAmount())
                    .orderName(payment.getOrderName())
                    .orderId(payment.getOrderId())
                    .paymentDate(payment.getPaymentDate())
                    .customer(payment.getCustomer().getId())
                    .build();
            paymentSuccessRepository.save(paymentSuccess);

            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public List<PaymentSuccessDTO> findByPayHistory(Integer userId) {
        List<PaymentSuccess> paymentSuccessList = paymentSuccessRepository.findByCustomer(userId);
        List<PaymentSuccessDTO> paymentSuccessDTOList = PaymentSuccessDTO.toPaymentSuccessDTO(paymentSuccessList);
        return paymentSuccessDTOList;
    }

    @Override
    public void AddCouponData(String orderName, Integer userId) {
        paymentSuccessDSLRepository.AddCouponData(orderName, userId);


        // 추가된 쿠폰의 수량에 따라 기존 추첨권의 상태를 업데이트
        List<Coupon> coupons = couponRepository.findAllByUserIdAndType(userId, "추첨권");

        for (Coupon coupon : coupons) {
            // 쿠폰이 사용된 상태로 표시되었지만 수량이 0이 아닌 경우
            if (coupon.getUsed() && coupon.getCnt() > 0) {
                // used 상태를 false로 업데이트
                coupon.setUsed(false);
                couponRepository.save(coupon);
            }
        }
    }
}
