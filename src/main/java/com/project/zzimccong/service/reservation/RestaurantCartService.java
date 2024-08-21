package com.project.zzimccong.service.reservation;

import com.project.zzimccong.model.dto.cart.CartDTO;
import com.project.zzimccong.model.dto.cart.CartResDTO;
import com.project.zzimccong.model.entity.cart.Cart;

import java.util.List;
import java.util.Map;

public interface RestaurantCartService {

    Cart saveRestaurantList(Cart restaurantList);
    List<CartResDTO> findByUserIdWithRestaurant(int userid);
    void deleteByStoreId(int userId, List<Integer> StoreIds);


}
