package com.project.zzimccong.service.store;

import com.project.zzimccong.model.entity.store.Restaurant;

import java.util.Optional;

import com.project.zzimccong.model.dto.store.RestaurantDTO;

import java.util.List;

public interface RestaurantService {
    void testChromeDriverWithCSSSelector();
    Optional<Restaurant> getRestaurantById(Long id);

    //1차 검색어로 가게 찾기
    public List<RestaurantDTO> findByKeyword(String keyword);

}
