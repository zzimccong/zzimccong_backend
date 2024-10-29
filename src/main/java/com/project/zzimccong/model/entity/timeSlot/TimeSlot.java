package com.project.zzimccong.model.entity.timeSlot;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.zzimccong.model.entity.store.Restaurant;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name="TB_TIMESLOT")
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @JsonBackReference(value = "restaurant-timeslots")
    private Restaurant restaurant;

    private LocalTime startTime; // 시작 시간
    private LocalTime endTime; // 종료 시간
    private int totalSeats; // 총 좌석 수
    private int availableSeats; // 남은 좌석 수

    public TimeSlot() {}

    public TimeSlot(Long id, Restaurant restaurant, LocalTime startTime, LocalTime endTime, int totalSeats, int availableSeats) {
        this.id = id;
        this.restaurant = restaurant;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
}

