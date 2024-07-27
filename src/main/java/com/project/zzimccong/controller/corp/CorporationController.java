package com.project.zzimccong.controller.corp;


import com.project.zzimccong.model.dto.corp.CorporationDTO;
import com.project.zzimccong.model.dto.email.EmailDTO;
import com.project.zzimccong.service.corp.CorporationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.zzimccong.model.entity.corp.Corporation;
import com.project.zzimccong.service.email.EmailVerificationService;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/corporations")
public class CorporationController {

    private final CorporationService corporationService;
    private final EmailVerificationService emailVerificationService;

    public CorporationController(CorporationService corporationService, EmailVerificationService emailVerificationService) {
        this.corporationService = corporationService;
        this.emailVerificationService = emailVerificationService;
    }

    @PostMapping("/corp-register")
    public ResponseEntity<String> registerCorporation(@RequestBody CorporationDTO corporationDTO) {
        if (corporationService.isCorpEmailExists(corporationDTO.getCorpEmail())) {
            return ResponseEntity.badRequest().body("Email already in use");
        }
        if (corporationService.isCorpIdExists(corporationDTO.getCorpId())) {
            return ResponseEntity.badRequest().body("ID already in use");
        }
        try {
            Corporation corporation = corporationService.registerCorporation(corporationDTO);
            return ResponseEntity.ok("Registration successful. Verification email sent to: " + corporation.getCorpEmail());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody CorporationDTO corporationDTO) {
        try {
            String token = corporationService.authenticate(corporationDTO.getCorpId(), corporationDTO.getPassword());
            if (token != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String email, @RequestParam String code) {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setCorpEmail(email);
        emailDTO.setVerificationCode(code);

        try {
            boolean isVerified = emailVerificationService.verifyCode(emailDTO);
            if (isVerified) {
                return ResponseEntity.ok("Email verified successfully.");
            } else {
                return ResponseEntity.badRequest().body("Invalid verification code.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error verifying email: " + e.getMessage());
        }
    }

    @GetMapping("/check-id")
    public ResponseEntity<Boolean> checkCorpIdExists(@RequestParam String corpId) {
        boolean exists = corporationService.isCorpIdExists(corpId);
        return ResponseEntity.ok(exists);
    }


    @GetMapping("/{corpId}")
    public ResponseEntity<Corporation> getCorporationById(@PathVariable String corpId) {
        try {
            Corporation corporation = corporationService.getCorporationById(corpId);
            return ResponseEntity.ok(corporation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{corpId}")
    public ResponseEntity<String> updateCorporation(@PathVariable String corpId, @RequestBody CorporationDTO corporationDTO) {
        try {
            corporationDTO.setCorpId(corpId);
            corporationService.updateCorporation(corporationDTO);
            return ResponseEntity.ok("Update successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
