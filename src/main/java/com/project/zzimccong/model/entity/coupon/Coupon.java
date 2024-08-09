package com.project.zzimccong.model.entity.coupon;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.zzimccong.model.entity.user.User;

import javax.persistence.*;

@Entity
@Table(name = "TB_COUPON")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "discount_price", nullable = true)
    private Integer discountPrice;

    @Column(name="cnt", nullable = false)
    private Integer cnt;

    public Coupon() {
    }

    public Coupon(Integer id, User user, String type, Integer discountPrice, Integer cnt) {
        this.id = id;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}