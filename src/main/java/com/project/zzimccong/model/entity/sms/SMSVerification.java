package com.project.zzimccong.model.entity.sms;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_SMS")
public class SMSVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "expiration_time", nullable = false)
    private LocalDateTime expirationTime;

    public SMSVerification() {}

    public SMSVerification(String phone, String code, LocalDateTime expirationTime) {
        this.phone = phone;
        this.code = code;
        this.expirationTime = expirationTime;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }
}
