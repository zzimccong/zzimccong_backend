package com.project.zzimccong.service.payment;

import com.project.zzimccong.model.entity.payment.Payment;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {
    Payment requestPayment(Payment payment, Integer userId);



}
