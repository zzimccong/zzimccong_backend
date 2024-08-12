package com.project.zzimccong.service.payment;

import com.project.zzimccong.model.entity.payment.Payment;
import com.project.zzimccong.model.entity.user.User;
import com.project.zzimccong.repository.payment.PaymentRepository;
import com.project.zzimccong.service.user.UserService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {


    private final PaymentRepository paymentRepository;
    private final UserService userService; // UserService 주입

    public PaymentServiceImpl(PaymentRepository paymentRepository, UserService userService) {
        this.paymentRepository = paymentRepository;
        this.userService = userService;
    }

    @Override
    public Payment requestPayment(Payment payment, Integer userId) {
        User user = userService.FindById(userId);
        if (user != null) {
            payment.setCustomer(user);
            return paymentRepository.save(payment);
        } else {
            throw new IllegalArgumentException("User not found with Id: " + userId);
        }
    }
}
