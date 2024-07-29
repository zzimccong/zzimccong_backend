package com.project.zzimccong.model.dto.sms;

public class SmsVerificationDTO {
    private String phone;
    private String verificationCode;

    public SmsVerificationDTO() {}

    public SmsVerificationDTO(String phone, String verificationCode) {
        this.phone = phone;
        this.verificationCode = verificationCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
