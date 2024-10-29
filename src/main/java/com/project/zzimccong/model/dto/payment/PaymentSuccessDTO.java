package com.project.zzimccong.model.dto.payment;


import com.project.zzimccong.model.dto.store.MenuDTO;
import com.project.zzimccong.model.entity.payment.Payment;
import com.project.zzimccong.model.entity.payment.PaymentSuccess;
import com.project.zzimccong.model.entity.store.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class PaymentSuccessDTO {


    private Long paymentId;

    private PayType payType;

    private Long amount;

    private String orderName;

    private String orderId;

    private LocalDateTime paymentDate;

    private Integer customer;

    private static PaymentSuccessDTO toPaymentSuccessDTO(PaymentSuccess paymentSuccess) {
        return PaymentSuccessDTO.builder()
                .paymentId(paymentSuccess.getPaymentId())
                .payType(paymentSuccess.getPayType())
                .amount(paymentSuccess.getAmount())
                .orderName(paymentSuccess.getOrderName())
                .orderId(paymentSuccess.getOrderId())
                .paymentDate(paymentSuccess.getPaymentDate())
                .build();
    }

    public static List<PaymentSuccessDTO> toPaymentSuccessDTO(List<PaymentSuccess> successList) {
        return successList.stream().map(PaymentSuccessDTO::toPaymentSuccessDTO).collect(Collectors.toList());
    }

}
