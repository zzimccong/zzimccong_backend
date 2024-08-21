//package com.project.zzimccong.model.dto.cart;
//
//
//import com.project.zzimccong.model.entity.cart.Cart;
//import com.project.zzimccong.model.entity.store.Restaurant;
//import com.project.zzimccong.model.entity.user.User;
//import lombok.Builder;
//
//@Builder
//public class CartDTO {
//
//    private Integer id;
//
//    private User user;
//
//    private Restaurant restaurant;
//
//    public CartDTO() {}
//
//    public CartDTO(Integer id, User user, Restaurant restaurant) {
//        this.id = id;
//        this.user = user;
//        this.restaurant = restaurant;
//    }
//
//    public CartDTO(Cart cart) {
//        this.id = cart.getId();
//        this.user = cart.getUser();
//        this.restaurant = cart.getRestaurant();
//    }
//
//    public Integer getId() {return id;}
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public User getUser() { return user; }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//
//    public Restaurant getRestaurant() { return restaurant; }
//
//    public void setRestaurant(Restaurant restaurant) {
//        this.restaurant = restaurant;
//    }
//
//}

package com.project.zzimccong.model.dto.cart;


import com.project.zzimccong.model.dto.store.MenuDTO;
import com.project.zzimccong.model.entity.cart.Cart;
import com.project.zzimccong.model.entity.store.Menu;
import com.project.zzimccong.model.entity.store.Restaurant;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public class CartResDTO {

    private Integer id;

    private Integer userId;

    private Restaurant restaurant;

    public CartResDTO() {}

    public CartResDTO(Integer id, Integer userId, Restaurant restaurant) {
        this.id = id;
        this.userId = userId;
        this.restaurant = restaurant;
    }

    public CartResDTO(Cart cart) {
        this.id = cart.getId();
        this.userId = cart.getUser().getId();
        this.restaurant = cart.getRestaurant();
    }

    public Integer getId() {return id;}

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() { return userId; }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Restaurant getRestaurant() { return restaurant; }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public static CartResDTO toCartResDTO(Cart cart) {
        return CartResDTO.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .restaurant(cart.getRestaurant())
                .build();
    }

    public static List<CartResDTO> toCartResDTO(List<Cart> cartList) {
        return cartList.stream().map(CartResDTO::toCartResDTO).collect(Collectors.toList());
    }

}

