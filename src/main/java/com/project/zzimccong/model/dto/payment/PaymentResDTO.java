package com.project.zzimccong.model.dto.payment;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PaymentResDTO {

    private String payType;
    private Long amount;
    private String orderName;
    private String orderId;
    private String customerEmail;
    private String customerName;
    private String successUrl;
    private String failUrl;

    //private String failReason;
    //private boolean cancelYN;
    //private String cancelReason;
    //private String createdAt;


    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public void setFailUrl(String failUrl) {
        this.failUrl = failUrl;
    }
}
