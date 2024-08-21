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

    public List<Cart> findByUserIdWithRestaurant(int userid){
        List<Cart> CartList = queryFactory
                             .selectFrom(cart)
                             .join(cart.restaurant, restaurant).fetchJoin()
                             .where(cart.user.id.eq(userid))
                             .fetch();

        for (Cart cart : CartList) {
            String restaurantName = cart.getRestaurant().getName(); // getRestaurant()과 getName()은 상황에 맞게 수정
            System.out.println("장바구니 항목의 레스토랑 이름: " + restaurantName);
        }
        return CartList;
    }

    public void deleteByStore(int userId, List<Integer> StoreIds){
        System.out.println("삭제할 레스토랑  "+StoreIds);
        List<Long> longStoreIds = StoreIds.stream().map(Long::valueOf).collect(Collectors.toList());
        queryFactory
                .delete(cart)
                .where(cart.user.id.eq(userId)
                        .and(cart.restaurant.id.in(longStoreIds)))
                .execute();

    }

}
