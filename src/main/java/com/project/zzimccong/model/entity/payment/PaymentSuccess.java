package com.project.zzimccong.model.entity.payment;


import com.project.zzimccong.model.dto.payment.PayType;
import com.project.zzimccong.model.dto.payment.PaymentResDTO;
import com.project.zzimccong.model.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="TB_PAYMENTSUCCESS")
public class PaymentSuccess {

    @Id
    @Column(nullable = false)
    private Long paymentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayType payType;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private String orderName;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private LocalDateTime paymentDate;

    @Column(nullable = false)
    private Integer customer;

    public Long getPaymentId() {
        return paymentId;
    }

    public PayType getPayType() {
        return payType;
    }

    public Long getAmount() {
        return amount;
    }

    public String getOrderName() {
        return orderName;
    }

    public String getOrderId() {
        return orderId;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public Integer getCustomer() {
        return customer;
    }
}
