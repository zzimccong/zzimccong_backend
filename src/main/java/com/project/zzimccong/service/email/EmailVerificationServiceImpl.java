package com.project.zzimccong.service.email;

import com.project.zzimccong.model.dto.email.EmailDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


import java.util.Random;

@Slf4j
@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final JavaMailSender javaMailSender;
    private final TemporaryStorageService temporaryStorageService;

    public EmailVerificationServiceImpl(JavaMailSender javaMailSender, TemporaryStorageService temporaryStorageService) {
        this.javaMailSender = javaMailSender;
        this.temporaryStorageService = temporaryStorageService;
    }

    @Override
    public void sendVerificationEmail(EmailDTO emailDTO) {
        String code = generateVerificationCode();
        temporaryStorageService.saveVerificationCode(emailDTO.getCorpEmail(), code);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(emailDTO.getCorpEmail());
        mailMessage.setSubject("찜꽁 테이블 이메일 인증 코드");

        mailMessage.setText("인증 코드: " + code);
        mailMessage.setFrom("ggg9905@naver.com"); // 발신자 주소 설정

        try {
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            System.out.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean verifyCode(EmailDTO emailDTO) {
        return temporaryStorageService.verifyCode(emailDTO.getCorpEmail(), emailDTO.getVerificationCode());
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(899999) + 100000;

//        테스트용 로그
        String verificationCode = String.valueOf(code);
        log.info("Generated verification code: " + verificationCode);

        return String.valueOf(code);
    }
}
