package com.project.zzimccong.model.dto.event;

public class EventParticipationDTO {
    private Long id;
    private Integer userId;
    private Long eventId;
    private Integer couponId;
    private Boolean winner;
    private int usedCouponCount;

    // 기본 생성자
    public EventParticipationDTO() {
    }

    // 모든 필드를 받는 생성자
    public EventParticipationDTO(Long id, Integer userId, Long eventId, Integer couponId, Boolean winner, int usedCouponCount) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.couponId = couponId;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
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

    @Override
    public String toString() {
        return "EventParticipationDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", eventId=" + eventId +
                ", couponId=" + couponId +
                ", winner=" + winner +
                ", usedCouponCount=" + usedCouponCount +
                '}';
    }
}