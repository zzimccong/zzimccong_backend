package com.project.zzimccong.service.reservation;

import com.project.zzimccong.model.dto.cart.CartDTO;
import com.project.zzimccong.model.dto.cart.CartResDTO;
import com.project.zzimccong.model.entity.cart.Cart;
import com.project.zzimccong.model.entity.store.Restaurant;
import com.project.zzimccong.repository.reservation.RestaurantCartDSLRepository;
import com.project.zzimccong.repository.reservation.RestaurantCartRepository;
import com.project.zzimccong.service.store.RestaurantServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantCartServiceImpl implements RestaurantCartService {

    @Autowired
    private RestaurantCartRepository restaurantCartRepository;
    @Autowired
    private RestaurantCartDSLRepository restaurantCartDSLRepository;
    @Autowired
    private RestaurantServiceImpl restaurantServiceImpl;

    @Override
    public Cart saveRestaurantList(CartDTO cartDto) {
        Restaurant restaurant = restaurantServiceImpl.findById(cartDto.getRestaurantId());

        Cart cart = new Cart();
        cart.setUser(cartDto.getUserId());
        cart.setRestaurant(restaurant);
        return restaurantCartRepository.save(cart);
    }

    @Override
    public List<CartResDTO> findByUserIdWithRestaurant(int userid) {
        List<Cart> cartList = restaurantCartDSLRepository.findByUserIdWithRestaurant(userid);
        List<CartResDTO> cartResDTOList = CartResDTO.toCartResDTO(cartList);
        return cartResDTOList;
    }

    @Override
    public void deleteByStoreId(int userId, List<Integer> storeIds) {
        restaurantCartDSLRepository.deleteByStore(userId, storeIds);
    }


}
