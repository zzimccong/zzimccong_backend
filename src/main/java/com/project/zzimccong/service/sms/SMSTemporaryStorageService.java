package com.project.zzimccong.service.sms;

import com.project.zzimccong.model.entity.sms.SMSVerification;
import com.project.zzimccong.repository.sms.SMSVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SMSTemporaryStorageService {

    private final SMSVerificationRepository smsVerificationRepository;

    @Autowired
    public SMSTemporaryStorageService(SMSVerificationRepository smsVerificationRepository) {
        this.smsVerificationRepository = smsVerificationRepository;
    }

    public void saveVerificationCode(String phone, String code) {
        SMSVerification smsVerification = new SMSVerification(phone, code, LocalDateTime.now().plusMinutes(10));
        smsVerificationRepository.save(smsVerification);
    }

    public boolean verifyCode(String phone, String code) {
        Optional<SMSVerification> verificationCodeOptional = smsVerificationRepository.findByPhoneAndCode(phone, code);
        if (verificationCodeOptional.isPresent()) {
            SMSVerification verificationCode = verificationCodeOptional.get();
            if (verificationCode.getExpirationTime().isAfter(LocalDateTime.now())) {
                smsVerificationRepository.delete(verificationCode);
                return true;
            }
        }
        return false;
    }

    public void deleteVerificationCode(String phone) {
        smsVerificationRepository.deleteByPhone(phone);
    }
}
