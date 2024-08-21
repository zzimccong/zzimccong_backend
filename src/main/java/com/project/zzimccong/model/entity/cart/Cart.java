package com.project.zzimccong.model.entity.cart;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.zzimccong.model.entity.store.Restaurant;

import javax.persistence.*;

@Entity
@Table(name="TB_CART")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonBackReference(value = "restaurant-lists")
    private Restaurant restaurant;

    public Cart() {}

    public Cart(Integer id, Integer userId, Restaurant restaurant) {
        this.id = id;
        this.userId = userId;
        this.restaurant = restaurant;
    }

    public Integer getId() {return id;}

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() { return userId; }

    public void setUser(Integer userId) {
        this.userId = userId;
    }

    public Restaurant getRestaurant() { return restaurant; }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}

