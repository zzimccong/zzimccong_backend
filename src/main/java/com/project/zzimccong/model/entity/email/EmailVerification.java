package com.project.zzimccong.model.entity.email;

import com.project.zzimccong.model.entity.corp.Corporation;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_EMAIL")
public class EmailVerification implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "corp_email", nullable = false)
    private String corpEmail;

    @Column(name = "verification_code", nullable = false)
    private String verificationCode;

    @Column(name = "expiration_time", nullable = false)
    private LocalDateTime expirationTime;

    @Column(name = "verified", nullable = false)
    private Boolean verified = false;

    @ManyToOne
    @JoinColumn(name = "corp_email", referencedColumnName = "corp_email", insertable = false, updatable = false)
    private Corporation corporation;

    public EmailVerification() {}

    public EmailVerification(Long id, String corpEmail, String verificationCode, LocalDateTime expirationTime, Boolean verified, Corporation corporation) {
        this.id = id;
        this.corpEmail = corpEmail;
        this.verificationCode = verificationCode;
        this.expirationTime = expirationTime;
        this.verified = verified;
        this.corporation = corporation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Corporation getCorporation() {
        return corporation;
    }

    public void setCorporation(Corporation corporation) {
        this.corporation = corporation;
    }
}
