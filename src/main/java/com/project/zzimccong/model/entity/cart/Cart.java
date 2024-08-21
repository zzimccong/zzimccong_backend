//package com.project.zzimccong.model.entity.cart;
//
//import com.project.zzimccong.model.entity.store.Restaurant;
//import com.project.zzimccong.model.entity.user.User;
//
//import javax.persistence.*;
//
//@Entity
//@Table(name="TB_CART")
//public class Cart {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
////    @ManyToOne(fetch = FetchType.LAZY)
////    @JoinColumn(name= "user_id", nullable = false)
////    private User user;
//
//    private Integer userId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "restaurant_id", nullable = false)
//    private Restaurant restaurant;
//
//    public Cart() {}
//
//    public Cart(Integer id, Integer userId, Restaurant restaurant) {
//        this.id = id;
//        this.userId = userId;
//        this.restaurant = restaurant;
//    }
//
//    public Integer getId() {return id;}
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public Integer getUserId() { return userId; }
//
//    public void setUserId(Integer userId) {
//        this.userId = userId;
//    }
//
//    public Restaurant getRestaurant() { return restaurant; }
//
//    public void setRestaurant(Restaurant restaurant) {
//        this.restaurant = restaurant;
//    }
//}

package com.project.zzimccong.model.entity.cart;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.zzimccong.model.entity.store.Restaurant;
import com.project.zzimccong.model.entity.user.User;

import javax.persistence.*;

@Entity
@Table(name="TB_CART")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name= "user_id", nullable = false)
    @JsonBackReference(value = "user-lists")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonBackReference(value = "restaurant-lists")
    private Restaurant restaurant;

    public Cart() {}

    public Cart(Integer id, User user, Restaurant restaurant) {
        this.id = id;
        this.user = user;
        this.restaurant = restaurant;
    }

    public Integer getId() {return id;}

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() { return user; }

    public void setUser(User user) {
        this.user = user;
    }

    public Restaurant getRestaurant() { return restaurant; }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}

