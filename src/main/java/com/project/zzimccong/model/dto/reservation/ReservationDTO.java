package com.project.zzimccong.model.dto.reservation;

import java.time.LocalDateTime;

public class ReservationDTO {

    private Long id;
    private Long restaurantId;
    private LocalDateTime reservationTime; //예약 시간
    private LocalDateTime reservationRegistrationTime; // 예약등록 시간
    private int count; // 인원 수
    private String state; // 예약 상태
    private String request; // 요청 사항

    public ReservationDTO() {}

    public ReservationDTO(Long id, Long restaurantId, LocalDateTime reservationTime, LocalDateTime reservationRegistrationTime, int count, String state, String request) {
        this.id = id;
        this.restaurantId = restaurantId;
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

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
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
}
