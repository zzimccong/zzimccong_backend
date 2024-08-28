package com.project.zzimccong.model.dto.ask;

import com.project.zzimccong.model.entity.ask.Answer;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class AnswerDTO {

    private Integer ask_id;
    private String role;
    private Integer userId;
    private Integer corpId;
    private String userName;
    private String corpName;
    private String content;

    public AnswerDTO() {}

    public Integer getAsk_id() {
        return ask_id;
    }

    public void setAsk_id(Integer ask_id) {
        this.ask_id = ask_id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "AnswerDTO{" +
                "ask_id=" + ask_id +
                ", role='" + role + '\'' +
                ", userId=" + userId +
                ", corpId=" + corpId +
                ", userName='" + userName + '\'' +
                ", corpName='" + corpName + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public static AnswerDTO toAnswerDTO(Answer answer) {
        return AnswerDTO.builder()
                .ask_id(answer.getAskId())
                .userId(answer.getUser_id())
                .corpId(answer.getCorp_id())
                .userName(answer.getUserName())
                .corpName(answer.getCorpName())
                .content(answer.getContent())
                .build();
    }

}
