package com.project.zzimccong.service.store;

import com.project.zzimccong.model.entity.store.Restaurant;

import java.util.Optional;

public interface RestaurantService {
    void testChromeDriverWithCSSSelector();
    Optional<Restaurant> getRestaurantById(Long id);
}
