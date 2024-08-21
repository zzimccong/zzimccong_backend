package com.project.zzimccong.controller.payment;

import com.project.zzimccong.config.TossPaymentConfig;
import com.project.zzimccong.model.dto.payment.PaymentDTO;
import com.project.zzimccong.model.dto.payment.PaymentResDTO;
import com.project.zzimccong.model.dto.payment.PaymentSuccessDTO;
import com.project.zzimccong.service.payment.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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


//    결제 성공시 로직
    @PostMapping("/success")
    public ResponseEntity<?> PaymentSuccess(@RequestBody Map<String, Object> requestData) {
        String orderId = (String) requestData.get("orderId");
        String orderName = (String) requestData.get("orderName");
        Integer userId = (Integer) requestData.get("userId");
        System.out.println("결제내역" + orderName);
        paymentService.AddSuccessData(orderId);
        paymentService.AddCouponData(orderName, userId);
        return ResponseEntity.ok("결제 정보 저장 완료");

    }

    //결제내역 조회
    @GetMapping("history/{user_id}")
    public List<PaymentSuccessDTO> getPayHistory(@PathVariable Integer user_id) {
        List<PaymentSuccessDTO> dto = paymentService.findByPayHistory(user_id);
        System.out.println("컨트롤러 결제내역 "+dto);
        return paymentService.findByPayHistory(user_id);
    }
}
