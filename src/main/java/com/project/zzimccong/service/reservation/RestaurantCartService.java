package com.project.zzimccong.service.reservation;

import com.project.zzimccong.model.dto.cart.CartDTO;
import com.project.zzimccong.model.dto.cart.CartResDTO;
import com.project.zzimccong.model.entity.cart.Cart;

import java.util.List;

public interface RestaurantCartService {

    Cart saveRestaurantList(CartDTO cartDto);
    List<CartResDTO> findByUserIdWithRestaurant(int userid);
    void deleteByStoreId(int userId, List<Integer> StoreIds);


}
