package com.project.zzimccong.repository.store;


import com.project.zzimccong.model.entity.store.Restaurant;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import static com.project.zzimccong.model.entity.store.QRestaurant.restaurant;
import static com.project.zzimccong.model.entity.store.QMenu.menu;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RestaurantDSLRepository {

    private final JPAQueryFactory queryFactory;

    //1차 검색(식당이름, 주소, 메뉴명, 카테고리)
    public List<Restaurant> findByKeyword(String keyword){
        System.out.println("검색어: " + keyword);
        List<Restaurant> Restaurants = queryFactory
                .selectDistinct(restaurant)
                .from(restaurant)
                .leftJoin(restaurant.menus , menu)
                .where(   restaurant.name.contains(keyword)
                        .or(restaurant.numberAddress.contains(keyword))
                        .or(restaurant.category.contains(keyword))
                        .or(restaurant.roadAddress.contains(keyword))
                                .or(menu.name.contains(keyword))
                        )
                        .fetch();

        System.out.println("가게"+ Restaurants);
        return Restaurants;

    }
}
