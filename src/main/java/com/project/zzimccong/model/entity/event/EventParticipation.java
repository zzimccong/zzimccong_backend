package com.project.zzimccong.model.entity.event;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.zzimccong.model.dto.event.EventParticipationDTO;
import com.project.zzimccong.model.entity.coupon.Coupon;
import com.project.zzimccong.model.entity.user.User;

import javax.persistence.*;

@Entity
@Table(name = "TB_EVENT_PARTICIPATION")
public class EventParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id", nullable = false)
    @JsonBackReference
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "coupon_id", nullable = false)
    @JsonBackReference
    private Coupon coupon;

    @Column(name = "winner", nullable = false)
    private Boolean winner = false;

    @Column(name = "used_coupon_count", nullable = false)
    private int usedCouponCount;

    // 기본 생성자
    public EventParticipation() {}

    // 모든 필드를 인자로 받는 생성자
    public EventParticipation(User user, Event event, Coupon coupon, Boolean winner, int usedCouponCount) {
        this.user = user;
        this.event = event;
        this.coupon = coupon;
        this.winner = winner;
        this.usedCouponCount = usedCouponCount;
    }

    // Getter 및 Setter 메서드
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

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public Boolean getWinner() {
        return winner;
    }

    public void setWinner(Boolean winner) {
        this.winner = winner;
    }

    public int getUsedCouponCount() {
        return usedCouponCount;
    }

    public void setUsedCouponCount(int usedCouponCount) {
        this.usedCouponCount = usedCouponCount;
    }

    // Entity를 DTO로 변환
    public static EventParticipationDTO fromEntity(EventParticipation participation) {
        return new EventParticipationDTO(
                participation.getId(),
                participation.getUser() != null ? participation.getUser().getId() : null,
                participation.getEvent() != null ? participation.getEvent().getId() : null,
                participation.getCoupon() != null ? participation.getCoupon().getId() : null,
                participation.getWinner(),
                participation.getUsedCouponCount()
        );
    }

    // DTO를 Entity로 변환
    public static EventParticipation toEntity(EventParticipationDTO dto, User user, Event event, Coupon coupon) {
        EventParticipation participation = new EventParticipation();
        participation.setId(dto.getId());
        participation.setUser(user);
        participation.setEvent(event);
        participation.setCoupon(coupon);
        participation.setWinner(dto.getWinner());
        participation.setUsedCouponCount(dto.getUsedCouponCount());
        return participation;
    }
}
