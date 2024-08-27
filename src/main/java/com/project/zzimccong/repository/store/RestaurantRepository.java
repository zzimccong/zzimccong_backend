package com.project.zzimccong.repository.store;


import com.project.zzimccong.model.entity.store.Restaurant;
import com.project.zzimccong.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByNameContaining(String name);
    List<Restaurant> findByUserId(Integer user_id);


    // 특정 레스토랑 ID와 역할이 "manager"인 모든 유저를 찾는 메서드
    @Query("SELECT u FROM User u JOIN u.restaurants r WHERE r.id = :restaurantId AND u.role = 'manager'")
    List<User> findManagersByRestaurantId(@Param("restaurantId") Long restaurantId);

}
