package com.project.zzimccong.model.dto.review;

public class ReviewDTO {
    private Long reservationId;
    private String content;
    private Integer taste;
    private Integer mood;
    private Integer convenient;
    private Double rate;
    private String restaurantName;
    private String userName;
    private String corpName;

    // 기본 생성자
    public ReviewDTO() {}

    // 생성자
    public ReviewDTO(Long reservationId, String content, Integer taste, Integer mood, Integer convenient, Double rate, String restaurantName, String userName, String corpName) {
        this.reservationId = reservationId;
        this.content = content;
        this.taste = taste;
        this.mood = mood;
        this.convenient = convenient;
        this.rate=rate;
        this.restaurantName=restaurantName;
        this.userName=userName;
        this.corpName=corpName;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
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

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
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
}
