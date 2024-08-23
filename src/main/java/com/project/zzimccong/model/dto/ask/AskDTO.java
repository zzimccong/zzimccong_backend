package com.project.zzimccong.model.dto.ask;

public class AskDTO {
    private String role;
    private Integer userId;
    private Integer corpId;
    private String userName;
    private String corpName;
    private String title;
    private String content;
    private Boolean secret;
    private String askPassword;

    public AskDTO() {
    }

    public AskDTO(String role, Integer userId, Integer corpId, String userName, String corpName, String title, String content, Boolean secret, String askPassword) {
        this.role = role;
        this.userId = userId;
        this.corpId = corpId;
        this.userName = userName;
        this.corpName = corpName;
        this.title = title;
        this.content = content;
        this.secret = secret;
        this.askPassword = askPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCorpId() {
        return corpId;
    }

    public void setCorpId(Integer corpId) {
        this.corpId = corpId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getSecret() {
        return secret;
    }

    public void setSecret(Boolean secret) {
        this.secret = secret;
    }

    public String getAskPassword() {
        return askPassword;
    }

    public void setAskPassword(String askPassword) {
        this.askPassword = askPassword;
    }
}
