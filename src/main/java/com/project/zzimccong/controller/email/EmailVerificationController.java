package com.project.zzimccong.controller.email;

import com.project.zzimccong.model.dto.email.EmailDTO;
import com.project.zzimccong.service.email.EmailVerificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/email")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    public EmailVerificationController(EmailVerificationService emailVerificationService) {
        this.emailVerificationService = emailVerificationService;
    }

    @PostMapping("/send-verification")
    public ResponseEntity<String> sendVerificationEmail(@RequestBody EmailDTO emailDTO) {
        try {
            emailVerificationService.sendVerificationEmail(emailDTO);
            return ResponseEntity.ok("Verification email sent");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error sending verification email: " + e.getMessage());
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<Boolean> verifyCode(@RequestBody EmailDTO emailDTO) {
        try {
            boolean result = emailVerificationService.verifyCode(emailDTO);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(false);
        }
    }
}
