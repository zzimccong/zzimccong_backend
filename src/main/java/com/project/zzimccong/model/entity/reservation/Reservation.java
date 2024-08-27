package com.project.zzimccong.model.entity.reservation;

import com.fasterxml.jackson.annotation.*;
import com.project.zzimccong.model.entity.corp.Corporation;
import com.project.zzimccong.model.entity.store.Restaurant;
import com.project.zzimccong.model.entity.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="TB_RESERVATION")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})  // 이 부분 추가
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonBackReference  // 고유한 참조 이름 설정
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")
    @JsonBackReference // 고유한 참조 이름 설정
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "corp_id")
    @JsonBackReference // 고유한 참조 이름 설정
    private Corporation corporation;



    private LocalDateTime reservationTime; //예약 시간
    private LocalDateTime reservationRegistrationTime; // 예약등록 시간
    private int count; // 인원 수
    private String state; // 예약 상태
    private String request; // 요청 사항

    public Reservation() {}

    public Reservation(Long id, User user, Restaurant restaurant, Corporation corporation, LocalDateTime reservationTime, LocalDateTime reservationRegistrationTime, int count, String state, String request) {
        this.id = id;
        this.user = user;
        this.restaurant = restaurant;
        this.corporation = corporation;
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

    public Corporation getCorporation() {
        return corporation;
    }

    public void setCorporation(Corporation corporation) {
        this.corporation = corporation;
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
                ", restaurant=" + restaurant +
                ", corporation=" + corporation +
                ", reservationTime=" + reservationTime +
                ", reservationRegistrationTime=" + reservationRegistrationTime +
                ", count=" + count +
                ", state='" + state + '\'' +
                ", request='" + request + '\'' +
                '}';
    }
}
