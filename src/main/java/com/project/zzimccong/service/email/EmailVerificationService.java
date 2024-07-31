package com.project.zzimccong.service.email;


import com.project.zzimccong.model.dto.email.EmailDTO;

public interface EmailVerificationService {
    void sendVerificationEmail(EmailDTO emailDTO);
    boolean verifyCode(EmailDTO emailDTO);
    void sendTemporaryPassword(String corpId, String userId, String email);
}
