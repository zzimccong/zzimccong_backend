package com.project.zzimccong.service.email;

import com.project.zzimccong.model.dto.email.EmailDTO;
import com.project.zzimccong.model.entity.corp.Corporation;
import com.project.zzimccong.model.entity.user.User;
import com.project.zzimccong.repository.corp.CorporationRepository;
import com.project.zzimccong.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;
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

    @Override
    public void sendTemporaryPassword(String corpId, String userId, String email) {
        if (corpId != null) {
            Optional<Corporation> corporationOptional = corporationRepository.findByCorpId(corpId);
            if (corporationOptional.isPresent()) {
                Corporation corporation = corporationOptional.get();
                if (corporation.getCorpEmail().equals(email)) {
                    String tempPassword = generateTemporaryPassword();
                    corporation.setPassword(passwordEncoder.encode(tempPassword));
                    corporationRepository.save(corporation);

                    SimpleMailMessage mailMessage = new SimpleMailMessage();
                    mailMessage.setTo(email);
                    mailMessage.setSubject("찜꽁 테이블 임시 비밀번호");
                    mailMessage.setText("임시 비밀번호 : " + tempPassword + "\n임시 비밀번호는 즉시 변경을 권장 드립니다.");
                    mailMessage.setFrom("ggg9905@naver.com");

                    try {
                        javaMailSender.send(mailMessage);
                    } catch (Exception e) {
                        System.out.println("이메일 발송 오류 : " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    throw new IllegalArgumentException("Email does not match");
                }
            } else {
                throw new IllegalArgumentException("Corporation not found");
            }
        }

        if (userId != null) {
            Optional<User> userOptional = userRepository.findByLoginId(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getEmail().equals(email)) {
                    String tempPassword = generateTemporaryPassword();
                    user.setPassword(passwordEncoder.encode(tempPassword));
                    userRepository.save(user);

                    SimpleMailMessage mailMessage = new SimpleMailMessage();
                    mailMessage.setTo(email);
                    mailMessage.setSubject("찜꽁 테이블 임시 비밀번호");
                    mailMessage.setText("임시 비밀번호 : " + tempPassword + "\n임시 비밀번호는 즉시 변경을 권장 드립니다.");
                    mailMessage.setFrom("ggg9905@naver.com");

                    try {
                        javaMailSender.send(mailMessage);
                    } catch (Exception e) {
                        System.out.println("이메일 발송 오류 : " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    throw new IllegalArgumentException("Email does not match");
                }
            } else {
                throw new IllegalArgumentException("User not found");
            }
        }
    }

    private String generateTemporaryPassword() {
        Random random = new Random();
        int password = 100000 + random.nextInt(900000);
        return String.valueOf(password);
    }
}
