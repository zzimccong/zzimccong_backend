package com.project.zzimccong.repository.store;


import com.project.zzimccong.model.entity.store.Restaurant;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import static com.project.zzimccong.model.entity.store.QRestaurant.restaurant;
import static com.project.zzimccong.model.entity.store.QMenu.menu;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    private BooleanExpression applyFilters(Map<String, Object> filters) {
        BooleanExpression predicate = restaurant.isNotNull();

        if (filters.containsKey("나라별") && !((List<String>) filters.get("나라별")).isEmpty()) {
            List<String> categorys = (List<String>) filters.get("나라별");
            for (String category : categorys) {
                predicate = predicate.and(restaurant.category.contains(category));
            }
        }

        if (filters.containsKey("테이블 타입") && !((List<String>) filters.get("테이블 타입")).isEmpty()) {
            List<String> tableTypes = (List<String>) filters.get("테이블 타입");
            for (String tableType : tableTypes) {
                predicate = predicate.and(restaurant.seats.contains(tableType));
            }
        }

        if (filters.containsKey("편의시설") && !((List<String>) filters.get("편의시설")).isEmpty()) {
            List<String> facilities = (List<String>) filters.get("편의시설");
            BooleanBuilder facilityPredicate = new BooleanBuilder();
            for (String facility : facilities) {
                facilityPredicate.or(restaurant.parkingInfo.contains(facility));
            }
            predicate = predicate.and(facilityPredicate);
        }
        return predicate;
    }

    //2차 필터(나라별, 편의시설, 좌석, 분위기)
    public List<Restaurant> findByFilter(Map<String, Object> filters){
        System.out.println("DSL "+filters);

        if (filters.containsKey("나라별")) {
            List<String> category = (List<String>) filters.get("나라별");
            if (category.contains("양식")) {
                category.add("이탈리아");
            }
            if (category.contains("아시아 음식")) {
                category.addAll(Arrays.asList("베트남", "인도"));
                category.remove("아시아 음식");
            }
            if (category.contains("아메리칸 음식")) {
                category.add("피자");
                category.remove("아메리칸 음식");
            }
        }
        if (filters.containsKey("편의시설")) {
            List<String> facilities = (List<String>) filters.get("편의시설");
            facilities.removeAll(Arrays.asList("그릴링 서비스", "콜키지 가능", "반려동물 동반 가능",
                    "장애인 편의시설"));
            if (facilities.contains("주차 가능")){
                facilities.add("주차가능");
            }
        }
        System.out.println("조건 거친 후 DSL: " + filters);

        List<Restaurant> restaurantsBefore = findByKeyword((String) filters.get("searchWord"));
        List<Restaurant> restaurantsAfter = queryFactory
                .selectDistinct(restaurant)
                .from(restaurant)
                .where(restaurant.in(restaurantsBefore)
                        .and(applyFilters(filters)))
                .fetch();
        return restaurantsAfter;
    }


}
