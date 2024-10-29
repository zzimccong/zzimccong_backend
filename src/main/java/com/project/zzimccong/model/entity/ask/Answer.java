package com.project.zzimccong.model.entity.ask;

import com.project.zzimccong.model.dto.ask.AnswerDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;

@Entity
@Table(name = "TB_Answer")
@Builder
@AllArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ask_id",nullable = false)
    private Integer askId;

    @Column(nullable = true)
    private Integer user_id;

    @Column(nullable = true)
    private Integer corp_id;

    @Column(nullable = true)
    private String userName;

    @Column(nullable = true)
    private String corpName;

    @Column(nullable = false)
    private String content;

    public Answer() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAskId() {
        return askId;
    }

    public void setAskId(Integer askId) {
        this.askId = askId;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getCorp_id() {
        return corp_id;
    }

    public void setCorp_id(Integer corp_id) {
        this.corp_id = corp_id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static Answer toEntity(AnswerDTO dto){
        return Answer.builder()
                .askId(dto.getAsk_id())
                .user_id(dto.getUserId())
                .corp_id(dto.getCorpId())
                .userName(dto.getUserName())
                .corpName(dto.getCorpName())
                .content(dto.getContent())
                .build();
    }
}
