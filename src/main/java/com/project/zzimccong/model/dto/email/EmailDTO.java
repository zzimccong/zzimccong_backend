package com.project.zzimccong.model.dto.email;

public class EmailDTO {
    private Long coId;
    private String corpEmail;
    private String verificationCode;

    public EmailDTO() {}

    public EmailDTO(Long coId, String corpEmail, String verificationCode) {
        this.coId = coId;
        this.corpEmail = corpEmail;
        this.verificationCode = verificationCode;
    }

    public Long getCoId() {
        return coId;
    }

    public void setCoId(Long coId) {
        this.coId = coId;
    }

    public String getCorpEmail() {
        return corpEmail;
    }

    public void setCorpEmail(String corpEmail) {
        this.corpEmail = corpEmail;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    @Override
    public String toString() {
        return "EmailDTO{" +
                "coId=" + coId +
                ", corpEmail='" + corpEmail + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                '}';
    }
}
