package com.project.zzimccong.model.entity.reservation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.zzimccong.model.entity.store.Restaurant;
import com.project.zzimccong.model.entity.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})  // 이 부분 추가
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-reservations")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @JsonBackReference(value = "restaurant-reservations")
    private Restaurant restaurant;

    private LocalDateTime reservationTime; //예약 시간
    private LocalDateTime reservationRegistrationTime; // 예약등록 시간
    private int count; // 인원 수
    private String state; // 예약 상태
    private String request; // 요청 사항

    public Reservation() {}

    public Reservation(Long id, User user, Restaurant restaurant, LocalDateTime reservationTime, LocalDateTime reservationRegistrationTime, int count, String state, String request) {
        this.id = id;
        this.user = user;
        this.restaurant = restaurant;
        this.reservationTime = reservationTime;
        this.reservationRegistrationTime = reservationRegistrationTime;
        this.count = count;
        this.state = state;
        this.request = request;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalDateTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public LocalDateTime getReservationRegistrationTime() {
        return reservationRegistrationTime;
    }

    public void setReservationRegistrationTime(LocalDateTime reservationRegistrationTime) {
        this.reservationRegistrationTime = reservationRegistrationTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", user=" + user +
                ", restaurant=" + restaurant +
                ", reservationTime=" + reservationTime +
                ", reservationRegistrationTime=" + reservationRegistrationTime +
                ", count=" + count +
                ", state='" + state + '\'' +
                ", request='" + request + '\'' +
                '}';
    }
}
