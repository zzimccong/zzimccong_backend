package com.project.zzimccong.model.dto.payment;

import com.project.zzimccong.model.entity.payment.Payment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class PaymentDTO {

    private PayType payType;

    private Long amount;

    private Integer userId;

    private String orderName;


    private String yourSuccessUrl;
    private String yourFailUrl;

    public Payment toEntity(){
        return Payment.builder()
                .payType(payType)
                .amount(amount)
                .orderName(orderName)
                .orderId(UUID.randomUUID().toString()) // 랜덤 주문 id 생성
                .paymentDate(LocalDateTime.now())
                .paySuccessYN(false)
                .build();
    }


}
