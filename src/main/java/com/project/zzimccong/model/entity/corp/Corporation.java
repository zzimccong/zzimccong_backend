package com.project.zzimccong.model.entity.corp;

import com.project.zzimccong.model.entity.email.EmailVerification;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "TB_CORPORATION")
public class Corporation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "corp_name")
    private String corpName;

    @Column(name = "corp_dept")
    private String corpDept;

    @Column(name = "corp_id")
    private String corpId;

    @Column(name = "passwd")
    private String password;

    @Column(name = "corp_email")
    private String corpEmail;

    @Column(name = "email_verified")
    private boolean emailVerified;

    @Column(name = "corp_address")
    private String corpAddress;

    @Column(name = "role")
    private String role;

    @OneToMany(mappedBy = "corporation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmailVerification> emailVerifications;

    public Corporation() {
    }

    public Corporation(Integer id, String corpName, String corpDept, String corpId, String password, String corpEmail, boolean emailVerified, String corpAddress, String role, List<EmailVerification> emailVerifications) {
        this.id = id;
        this.corpName = corpName;
        this.corpDept = corpDept;
        this.corpId = corpId;
        this.password = password;
        this.corpEmail = corpEmail;
        this.emailVerified = emailVerified;
        this.corpAddress = corpAddress;
        this.role = role;
        this.emailVerifications = emailVerifications;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getCorpDept() {
        return corpDept;
    }

    public void setCorpDept(String corpDept) {
        this.corpDept = corpDept;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCorpEmail() {
        return corpEmail;
    }

    public void setCorpEmail(String corpEmail) {
        this.corpEmail = corpEmail;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getCorpAddress() {
        return corpAddress;
    }

    public void setCorpAddress(String corpAddress) {
        this.corpAddress = corpAddress;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<EmailVerification> getEmailVerifications() {
        return emailVerifications;
    }

    public void setEmailVerifications(List<EmailVerification> emailVerifications) {
        this.emailVerifications = emailVerifications;
    }

    @Override
    public String toString() {
        return "Corporation{" +
                "id=" + id +
                ", corpName='" + corpName + '\'' +
                ", corpDept='" + corpDept + '\'' +
                ", corpId='" + corpId + '\'' +
                ", password='" + password + '\'' +
                ", corpEmail='" + corpEmail + '\'' +
                ", emailVerified=" + emailVerified +
                ", corpAddress='" + corpAddress + '\'' +
                ", role='" + role + '\'' +
                ", emailVerifications=" + emailVerifications +
                '}';
    }
}
