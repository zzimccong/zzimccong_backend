package com.project.zzimccong.model.dto.coupon;

import com.project.zzimccong.model.entity.coupon.Coupon;

public class CouponDTO {
    private Integer id;
    private Integer userId;
    private String type;
    private Integer discountPrice;
    private Integer cnt;

    private Long eventId;
    private Boolean used;

    public CouponDTO() {
    }

    public CouponDTO(Integer id, Integer userId, String type, Integer discountPrice, Integer cnt, Long eventId, Boolean used) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.discountPrice = discountPrice;
        this.cnt = cnt;
        this.eventId = eventId;
        this.used = used;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Integer discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public static CouponDTO fromEntity(Coupon coupon) {
        return new CouponDTO(
                coupon.getId(),
                coupon.getUser().getId(),
                coupon.getType(),
                coupon.getDiscountPrice(),
                coupon.getCnt(),
                coupon.getEvent() != null ? coupon.getEvent().getId() : null,
                coupon.getUsed()
        );
    }

    public static Coupon toEntity(CouponDTO couponDTO) {
        Coupon coupon = new Coupon();
        coupon.setId(couponDTO.getId());
        coupon.setType(couponDTO.getType());
        coupon.setDiscountPrice(couponDTO.getDiscountPrice());
        coupon.setCnt(couponDTO.getCnt());
        coupon.setUsed(couponDTO.getUsed());
        // Event와 User는 별도의 로직에서 설정해야 합니다.
        return coupon;
    }

    @Override
    public String toString() {
        return "CouponDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", type='" + type + '\'' +
                ", discountPrice=" + discountPrice +
                ", cnt=" + cnt +
                ", eventId=" + eventId +
                ", used=" + used +
                '}';
    }
}