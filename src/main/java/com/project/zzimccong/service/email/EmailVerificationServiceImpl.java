package com.project.zzimccong.service.email;

import com.project.zzimccong.model.dto.email.EmailDTO;
import com.project.zzimccong.model.entity.corp.Corporation;
import com.project.zzimccong.model.entity.user.User;
import com.project.zzimccong.repository.corp.CorporationRepository;
import com.project.zzimccong.repository.user.UserRepository;
import com.project.zzimccong.service.redis.TemporaryStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final JavaMailSender javaMailSender;
    private final TemporaryStorageService temporaryStorageService;
    private final CorporationRepository corporationRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public EmailVerificationServiceImpl(JavaMailSender javaMailSender, TemporaryStorageService temporaryStorageService, CorporationRepository corporationRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.javaMailSender = javaMailSender;
        this.temporaryStorageService = temporaryStorageService;
        this.corporationRepository = corporationRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void sendVerificationEmail(EmailDTO emailDTO) {
        String code = generateVerificationCode();
        temporaryStorageService.saveEmailVerificationCode(emailDTO.getCorpEmail(), code);
        sendEmail(emailDTO.getCorpEmail(), "찜꽁 테이블 이메일 인증 코드", "인증 코드: " + code);
    }

    @Override
    public void resendVerificationEmail(EmailDTO emailDTO) {
        String newCode = generateVerificationCode();
        temporaryStorageService.resendEmailVerificationCode(emailDTO.getCorpEmail(), newCode);
        sendEmail(emailDTO.getCorpEmail(), "찜꽁 테이블 이메일 인증 코드 재전송", "인증 코드: " + newCode);
    }

    @Override
    public boolean verifyCode(EmailDTO emailDTO) {
        return temporaryStorageService.verifyEmailCode(emailDTO.getCorpEmail(), emailDTO.getVerificationCode());
    }

    @Override
    public void sendTemporaryPassword(String corpId, String userId, String email) {
        if (corpId != null) {
            handleTemporaryPasswordForCorp(corpId, email);
        }

        if (userId != null) {
            handleTemporaryPasswordForUser(userId, email);
        }
    }

    private void handleTemporaryPasswordForCorp(String corpId, String email) {
        Corporation corporation = corporationRepository.findByCorpId(corpId)
                .orElseThrow(() -> new IllegalArgumentException("Corporation not found"));

        if (!corporation.getCorpEmail().equals(email)) {
            throw new IllegalArgumentException("Email does not match");
        }

        String tempPassword = generateTemporaryPassword();
        corporation.setPassword(passwordEncoder.encode(tempPassword));
        corporationRepository.save(corporation);

        sendEmail(email, "찜꽁 테이블 임시 비밀번호", "임시 비밀번호 : " + tempPassword + "\n임시 비밀번호는 즉시 변경을 권장 드립니다.");
    }

    private void handleTemporaryPasswordForUser(String userId, String email) {
        User user = userRepository.findByLoginId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.getEmail().equals(email)) {
            throw new IllegalArgumentException("Email does not match");
        }

        String tempPassword = generateTemporaryPassword();
        user.setPassword(passwordEncoder.encode(tempPassword));
        userRepository.save(user);

        sendEmail(email, "찜꽁 테이블 임시 비밀번호", "임시 비밀번호 : " + tempPassword + "\n임시 비밀번호는 즉시 변경을 권장 드립니다.");
    }

    private String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(899999) + 100000);
    }

    private String generateTemporaryPassword() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        mailMessage.setFrom("ggg9905@naver.com");

        try {
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            log.error("이메일 발송 오류 : {}", e.getMessage(), e);
        }
    }
}
