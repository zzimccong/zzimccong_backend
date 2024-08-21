package com.project.zzimccong.repository.reservation;

import com.project.zzimccong.model.entity.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantCartRepository extends JpaRepository<Cart, Integer> {

}
