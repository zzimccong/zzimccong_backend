package com.project.zzimccong.controller.payment;

import com.project.zzimccong.config.TossPaymentConfig;
import com.project.zzimccong.model.dto.payment.PaymentDTO;
import com.project.zzimccong.model.dto.payment.PaymentResDTO;
import com.project.zzimccong.service.payment.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pay")
public class PaymentController {


    private final PaymentService paymentService;
    private final TossPaymentConfig tossPaymentConfig;

    public PaymentController(PaymentService paymentService,
                             TossPaymentConfig tossPaymentConfig) {
        this.paymentService = paymentService;
        this.tossPaymentConfig = tossPaymentConfig;
    }

    //결제 요청
    @PostMapping
    public ResponseEntity requestPayment(@RequestBody PaymentDTO paymentReqDto) {
//        System.out.println("받은거 " + paymentReqDto.getOrderName() +
//                paymentReqDto.getUserId());

        PaymentResDTO paymentResDto = paymentService.requestPayment(paymentReqDto.toEntity(), paymentReqDto.getUserId()).toPaymentResDTO();
        paymentResDto.setSuccessUrl(paymentReqDto.getYourSuccessUrl() == null ? tossPaymentConfig.getSuccessUrl() : paymentReqDto.getYourSuccessUrl());
        paymentResDto.setFailUrl(paymentReqDto.getYourFailUrl() == null ? tossPaymentConfig.getFailUrl() : paymentReqDto.getYourFailUrl());
//        System.out.println(paymentResDto.getCustomerName());
        return ResponseEntity.ok().body(paymentResDto);

    }


    //결제 성공시 로직
//    @GetMapping("/successs")
//    public ResponseEntity tossPaymentSuccess(@RequestParam String paymentKey,
//                                             @RequestParam String orderId,
//                                             @RequestParam Long amount) {
//
//
//    }
//
//    //결제 실패시 로직
//    public ResponseEntity tossPaymentFail() {
//
//    }
//
//    //결제내역 조회
//    public ResponseEntity getChargingHistory() {
//
//    }
}
