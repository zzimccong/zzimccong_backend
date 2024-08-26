package com.project.zzimccong.model.entity.event;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.zzimccong.model.entity.store.Restaurant;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "TB_EVENT")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonBackReference
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(name = "status", nullable = false)
    private String status;  // 'ONGOING', 'ENDED' 등의 상태 관리

    // 이벤트와 참가 기록의 관계 설정 (CascadeType.ALL 및 orphanRemoval 설정)
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventParticipation> participations;


    // 기본 생성자
    public Event() {}

    // 모든 필드를 인자로 받는 생성자
    public Event(Long id, LocalDate startDate, LocalDate endDate, Restaurant restaurant, String status, List<EventParticipation> participations ) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.restaurant = restaurant;
        this.status = status;
        this.participations = participations;
    }

    // Getter 및 Setter 메서드
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<EventParticipation> getParticipations() {
        return participations;
    }

    public void setParticipations(List<EventParticipation> participations) {
        this.participations = participations;
    }
}
