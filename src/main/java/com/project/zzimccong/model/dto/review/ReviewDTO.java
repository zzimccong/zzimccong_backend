package com.project.zzimccong.model.dto.review;

public class ReviewDTO {
    private Long id;
    private Long reservationId;
    private Integer userId;
    private Integer corpId;
    private String content;
    private Integer taste;
    private Integer mood;
    private Integer convenient;
    private Double rate;

    // 기본 생성자
    public ReviewDTO() {}

    // 생성자
    public ReviewDTO(Long id, Long reservationId, Integer userId, Integer corpId, String content, Integer taste, Integer mood, Integer convenient, Double rate) {
        this.id = id;
        this.reservationId = reservationId;
        this.userId = userId;
        this.corpId = corpId;
        this.content = content;
        this.taste = taste;
        this.mood = mood;
        this.convenient = convenient;
        this.rate = rate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getTaste() {
        return taste;
    }

    public void setTaste(Integer taste) {
        this.taste = taste;
    }

    public Integer getMood() {
        return mood;
    }

    public void setMood(Integer mood) {
        this.mood = mood;
    }

    public Integer getConvenient() {
        return convenient;
    }

    public void setConvenient(Integer convenient) {
        this.convenient = convenient;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
