package com.project.zzimccong.repository.reservation;

import com.project.zzimccong.model.entity.cart.Cart;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.project.zzimccong.model.entity.cart.QCart.cart;
import static com.project.zzimccong.model.entity.store.QRestaurant.restaurant;

@Repository
@RequiredArgsConstructor
@Transactional
public class RestaurantCartDSLRepository {

    private final JPAQueryFactory queryFactory;

    public List<Cart> findByUserIdWithRestaurant(int userId){
        List<Cart> CartList = queryFactory
                             .selectFrom(cart)
                             .join(cart.restaurant, restaurant).fetchJoin()
                             .where(cart.userId.eq(userId))
                             .fetch();

        for (Cart cart : CartList) {
            String restaurantName = cart.getRestaurant().getName();
        }
        return CartList;
    }

    public void deleteByStore(int userId, List<Integer> StoreIds){
        List<Long> longStoreIds = StoreIds.stream().map(Long::valueOf).collect(Collectors.toList());
        queryFactory
                .delete(cart)
                  .where(cart.userId.eq(userId)
                        .and(cart.restaurant.id.in(longStoreIds)))
                .execute();

    }

}
