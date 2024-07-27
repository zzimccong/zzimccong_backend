package com.project.zzimccong.repository.email;


import com.project.zzimccong.model.entity.email.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    Optional<EmailVerification> findByCorpEmailAndVerificationCode(String corpEmail, String verificationCode);
}
