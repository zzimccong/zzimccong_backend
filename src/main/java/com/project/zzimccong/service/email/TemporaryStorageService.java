package com.project.zzimccong.service.email;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TemporaryStorageService {

    private final Map<String, VerificationCode> emailVerificationStorage = new ConcurrentHashMap<>();

    public void saveVerificationCode(String email, String code) {
        emailVerificationStorage.put(email, new VerificationCode(code, LocalDateTime.now().plusMinutes(10)));
    }

    public boolean verifyCode(String email, String code) {
        VerificationCode verificationCode = emailVerificationStorage.get(email);
        if (verificationCode != null && verificationCode.getCode().equals(code) && verificationCode.getExpirationTime().isAfter(LocalDateTime.now())) {
            emailVerificationStorage.remove(email);
            return true;
        }
        return false;
    }

    @Getter
    private static class VerificationCode {
        private final String code;
        private final LocalDateTime expirationTime;

        VerificationCode(String code, LocalDateTime expirationTime) {
            this.code = code;
            this.expirationTime = expirationTime;
        }

    }
}
