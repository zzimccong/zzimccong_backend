package com.project.zzimccong.service.store;

import com.project.zzimccong.model.dto.store.RestaurantResDTO;
import com.project.zzimccong.model.entity.store.Restaurant;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import com.project.zzimccong.model.dto.store.RestaurantDTO;

import java.util.List;

public interface RestaurantService {
    void testChromeDriverWithCSSSelector();
    Optional<Restaurant> getRestaurantById(Long id);
    Restaurant createRestaurant(Restaurant restaurant);
    Restaurant updateRestaurant(Long id, Restaurant restaurantDetails);
    List<Restaurant> getRestaurantsByUserId(Integer user_id);

    //1차 검색어로 가게 찾기
    public List<RestaurantResDTO> findByKeyword(String keyword);
    //2차 키워드로 가게 필터
    public List<RestaurantResDTO> findByFilter(Map<String, Object> filters);
    public Restaurant findById(Long id);
    int getAvailableSeats(Long restaurantId, LocalDateTime reservationTime);


}
