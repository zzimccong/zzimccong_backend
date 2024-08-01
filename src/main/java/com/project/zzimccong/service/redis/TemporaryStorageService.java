package com.project.zzimccong.service.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class TemporaryStorageService {

    private final RedisTemplate<String, Object> redisTemplate;

    // 생성자 주입을 통해 RedisTemplate을 초기화
    public TemporaryStorageService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 이메일 인증 코드 저장
    public void saveEmailVerificationCode(String email, String code) {
        // 인증 코드와 만료 시간 설정
        VerificationCode verificationCode = new VerificationCode(code, LocalDateTime.now().plusMinutes(3));
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        // Redis에 인증 코드 저장, 3분 TTL 설정
        valueOperations.set("email:" + email, verificationCode, 3, TimeUnit.MINUTES);
        // 데이터가 저장되었는지 검증
        VerificationCode savedCode = (VerificationCode) valueOperations.get("email:" + email);
        assert savedCode != null;
        System.out.println("Saved Code: " + savedCode.getCode());
    }

    // 이메일 인증 코드 재전송
    public void resendEmailVerificationCode(String email, String newCode) {
        // 기존 인증 코드 삭제
        deleteEmailVerificationCode(email);
        // 새로운 인증 코드 저장
        saveEmailVerificationCode(email, newCode);
    }

    // 이메일 인증 코드 검증
    public boolean verifyEmailCode(String email, String code) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        VerificationCode verificationCode = (VerificationCode) valueOperations.get("email:" + email);
        if (verificationCode != null) {
            // 인증 코드와 만료 시간 검증
            if (verificationCode.getCode().equals(code) && verificationCode.getExpirationTime().isAfter(LocalDateTime.now())) {
                // 인증 성공 후 코드 삭제
                redisTemplate.delete("email:" + email);
                return true;
            } else if (verificationCode.getExpirationTime().isBefore(LocalDateTime.now())) {
                // 만료 시간이 지난 경우 키 삭제
                redisTemplate.delete("email:" + email);
            }
        }
        return false;
    }

    // 이메일 인증 코드 삭제
    public void deleteEmailVerificationCode(String email) {
        redisTemplate.delete("email:" + email);
    }

    // SMS 인증 코드 저장
    public void saveSMSVerificationCode(String phone, String code) {
        // 인증 코드와 만료 시간 설정
        VerificationCode verificationCode = new VerificationCode(code, LocalDateTime.now().plusMinutes(3));
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        // Redis에 인증 코드 저장, 3분 TTL 설정
        valueOperations.set("sms:" + phone, verificationCode, 3, TimeUnit.MINUTES);
        // 데이터가 저장되었는지 검증
        VerificationCode savedCode = (VerificationCode) valueOperations.get("sms:" + phone);
        assert savedCode != null;
        System.out.println("Saved Code: " + savedCode.getCode());
    }

    // SMS 인증 코드 재전송
    public void resendSMSVerificationCode(String phone, String newCode) {
        // 기존 인증 코드 삭제
        deleteSMSVerificationCode(phone);
        // 새로운 인증 코드 저장
        saveSMSVerificationCode(phone, newCode);
    }

    // SMS 인증 코드 검증
    public boolean verifySMSCode(String phone, String code) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        VerificationCode verificationCode = (VerificationCode) valueOperations.get("sms:" + phone);
        if (verificationCode != null) {
            // 인증 코드와 만료 시간 검증
            if (verificationCode.getCode().equals(code) && verificationCode.getExpirationTime().isAfter(LocalDateTime.now())) {
                // 인증 성공 후 코드 삭제
                redisTemplate.delete("sms:" + phone);
                return true;
            } else if (verificationCode.getExpirationTime().isBefore(LocalDateTime.now())) {
                // 만료 시간이 지난 경우 키 삭제
                redisTemplate.delete("sms:" + phone);
            }
        }
        return false;
    }

    // SMS 인증 코드 삭제
    public void deleteSMSVerificationCode(String phone) {
        redisTemplate.delete("sms:" + phone);
    }

    // 내부 클래스: 인증 코드와 만료 시간을 포함
    private static class VerificationCode implements java.io.Serializable {
        private final String code;
        private final LocalDateTime expirationTime;

        VerificationCode(String code, LocalDateTime expirationTime) {
            this.code = code;
            this.expirationTime = expirationTime;
        }

        public String getCode() {
            return code;
        }

        public LocalDateTime getExpirationTime() {
            return expirationTime;
        }
    }
}
