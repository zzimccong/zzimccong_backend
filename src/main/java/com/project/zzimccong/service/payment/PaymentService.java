package com.project.zzimccong.service.payment;

import com.project.zzimccong.model.dto.payment.PaymentSuccessDTO;
import com.project.zzimccong.model.entity.payment.Payment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentService {
    Payment requestPayment(Payment payment, Integer userId);
    int AddSuccessData(String orderId);
    List<PaymentSuccessDTO> findByPayHistory(Integer userId);
    void AddCouponData(String orderName, Integer userId);



}
