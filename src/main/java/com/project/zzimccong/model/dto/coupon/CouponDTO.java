package com.project.zzimccong.model.dto.coupon;

public class CouponDTO {
    private Integer id;
    private Integer userId;
    private String type;
    private Integer discountPrice;
    private Integer cnt;

    public CouponDTO() {
    }

    public CouponDTO(Integer id, Integer userId, String type, Integer discountPrice, Integer cnt) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.discountPrice = discountPrice;
        this.cnt = cnt;
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

    @Override
    public String toString() {
        return "CouponDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", type='" + type + '\'' +
                ", discountPrice=" + discountPrice +
                ", cnt=" + cnt +
                '}';
    }
}
