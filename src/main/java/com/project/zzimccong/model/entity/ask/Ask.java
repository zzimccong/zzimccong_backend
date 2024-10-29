package com.project.zzimccong.model.entity.ask;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.zzimccong.model.entity.corp.Corporation;
import com.project.zzimccong.model.entity.user.User;

import javax.persistence.*;

@Entity
@Table(name = "TB_ASK")
public class Ask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = true)
    @JsonBackReference(value = "ask-user")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "corp_id", nullable = true)
    @JsonBackReference(value = "ask-corporation")
    private Corporation corporation;

    private String title;

    private String content;

    private Boolean secret;

    private String askPassword;

    public Ask() {
    }

    public Ask(Integer id, User user, Corporation corporation, String title, String content, Boolean secret, String askPassword) {
        Id = id;
        this.user = user;
        this.corporation = corporation;
        this.title = title;
        this.content = content;
        this.secret = secret;
        this.askPassword = askPassword;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Corporation getCorporation() {
        return corporation;
    }

    public void setCorporation(Corporation corporation) {
        this.corporation = corporation;
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
