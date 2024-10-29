package com.project.zzimccong.util.sms;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SmsUtil {

    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecretKey;

    @Value("${coolsms.api.from}") // 발신번호를 application.properties에서 가져옴
    private String from;

    private DefaultMessageService messageService;

    @PostConstruct
    private void init() {
        if (apiKey == null || apiKey.length() != 16) {
            throw new IllegalArgumentException("API Key must be 16 characters long");
        }
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");
    }

    public void sendOne(String to, String verificationCode) {
        Message message = new Message();
        message.setFrom(from); // 발신번호 설정
        message.setTo(to);
        message.setText("[찜꽁테이블] 아래의 인증번호를 입력해주세요\n" + verificationCode);

        this.messageService.sendOne(new SingleMessageSendingRequest(message));
    }
}
