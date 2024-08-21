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


import com.project.zzimccong.model.entity.cart.Cart;
import lombok.Builder;

@Builder
public class CartDTO {

    private Integer id;

    private Integer userId;

    private Long restaurantId;

    public CartDTO() {}

    public CartDTO(Integer id, Integer userId, Long restaurantId) {
        this.id = id;
        this.userId = userId;
        this.restaurantId = restaurantId;
    }

    public CartDTO(Cart cart) {
        this.id = cart.getId();
        this.userId = cart.getId();
        this.restaurantId = cart.getRestaurant().getId();
    }

    public Integer getId() {return id;}

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() { return userId; }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getRestaurantId() { return restaurantId; }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

}

