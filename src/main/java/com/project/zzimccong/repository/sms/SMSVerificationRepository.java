package com.project.zzimccong.repository.sms;



import com.project.zzimccong.model.entity.sms.SMSVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SMSVerificationRepository extends JpaRepository<SMSVerification, Integer> {
    Optional<SMSVerification> findByPhoneAndCode(String phone, String code);
    void deleteByPhone(String phone);
}
