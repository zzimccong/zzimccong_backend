package com.project.zzimccong.repository.payment;

import com.project.zzimccong.model.entity.payment.Payment;
import com.project.zzimccong.model.entity.payment.PaymentSuccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentSuccessRepository extends JpaRepository<PaymentSuccess, Integer> {
    boolean existsByOrderId(String orderId);
    List<PaymentSuccess> findByCustomer(Integer customer);
}
