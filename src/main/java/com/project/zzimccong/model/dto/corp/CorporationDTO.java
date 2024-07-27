package com.project.zzimccong.model.dto.corp;

public class CorporationDTO {

    private String corpName;
    private String corpDept;
    private String corpId;
    private String password;
    private String corpEmail;
    private String corpAddress;
    private boolean emailVerified;
    private String role;

    public CorporationDTO() {}

    public CorporationDTO(String corpName, String corpDept, String corpId, String password, String corpEmail, String corpAddress, boolean emailVerified, String role) {
        this.corpName = corpName;
        this.corpDept = corpDept;
        this.corpId = corpId;
        this.password = password;
        this.corpEmail = corpEmail;
        this.corpAddress = corpAddress;
        this.emailVerified = emailVerified;
        this.role = role;
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

    public String getCorpAddress() {
        return corpAddress;
    }

    public void setCorpAddress(String corpAddress) {
        this.corpAddress = corpAddress;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "CorporationDTO{" +
                "corpName='" + corpName + '\'' +
                ", corpDept='" + corpDept + '\'' +
                ", corpId='" + corpId + '\'' +
                ", password='" + password + '\'' +
                ", corpEmail='" + corpEmail + '\'' +
                ", corpAddress='" + corpAddress + '\'' +
                ", emailVerified=" + emailVerified +
                ", role='" + role + '\'' +
                '}';
    }
}
