package com.project.zzimccong.model.entity.payment;


import com.project.zzimccong.model.dto.payment.PayType;
import com.project.zzimccong.model.dto.payment.PaymentResDTO;
import com.project.zzimccong.model.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="TB_PAYMENT", indexes = {
        @Index(name = "idx_payment_member", columnList="customer"),
        @Index(name = "idx_payment_paymentKey", columnList = "paymentKey"),
})
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
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

    private boolean paySuccessYN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer")
    private User customer;

    private String paymentKey;

    public void setCustomer(User customer) {
        this.customer = customer;
    }

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

    public boolean isPaySuccessYN() {
        return paySuccessYN;
    }

    public User getCustomer() {
        return customer;
    }

    public String getPaymentKey() {
        return paymentKey;
    }

    public PaymentResDTO toPaymentResDTO() {
        return PaymentResDTO.builder()
                .payType(payType.getDescription())
                .amount(amount)
                .orderName(orderName)
                .orderId(orderId)
                .customerEmail(customer.getEmail())
                .customerName(customer.getName())
                .build();
    }
}
