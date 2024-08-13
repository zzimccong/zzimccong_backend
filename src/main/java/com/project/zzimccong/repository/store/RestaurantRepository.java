package com.project.zzimccong.repository.store;


import com.project.zzimccong.model.entity.store.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByNameContaining(String name);
    List<Restaurant> findByUserId(Integer user_id);



}
