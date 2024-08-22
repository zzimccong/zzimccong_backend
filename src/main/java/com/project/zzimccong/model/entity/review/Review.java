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
    @JsonBackReference(value = "review-reservation")
    private Reservation reservation;

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

    public Review(Long id, Reservation reservation, String content, Integer taste, Integer mood, Integer convenient, Double rate) {
        this.id = id;
        this.reservation = reservation;
        this.content = content;
        this.taste = taste;
        this.mood = mood;
        this.convenient = convenient;
        this.rate = rate;
    }

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
