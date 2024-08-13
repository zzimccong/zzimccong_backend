package com.project.zzimccong.model.entity.review;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.zzimccong.model.entity.reservation.Reservation;

import javax.persistence.*;

@Entity
@Table(name = "TB_REVIEW")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reservation_id", nullable = false)
    @JsonBackReference
    private Reservation reservation;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "corp_id")
    private Integer corpId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "taste", nullable = false)
    private Integer taste;

    @Column(name = "mood", nullable = false)
    private Integer mood;

    @Column(name = "convenient", nullable = false)
    private Integer convenient;

    @Column(name = "rate", nullable = false)
    private Double rate;

    // 기본 생성자
    public Review() {}

    // 생성자
    public Review(Long id, Reservation reservation, Integer userId, Integer corpId, String content, Integer taste, Integer mood, Integer convenient, Double rate) {
        this.id = id;
        this.reservation = reservation;
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

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
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
