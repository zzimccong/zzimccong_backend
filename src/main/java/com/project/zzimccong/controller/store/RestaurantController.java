package com.project.zzimccong.controller.store;


import com.project.zzimccong.model.dto.store.RestaurantDTO;
import com.project.zzimccong.model.entity.store.Restaurant;
import com.project.zzimccong.repository.store.RestaurantRepository;
import com.project.zzimccong.service.store.RestaurantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

//@Controller
@RestController
@RequestMapping("/api")
public class RestaurantController {

    private RestaurantService restaurantService;
    private RestaurantRepository restaurantRepository;

    public RestaurantController(RestaurantService restaurantService, RestaurantRepository restaurantRepository) {
        this.restaurantService = restaurantService;
        this.restaurantRepository = restaurantRepository;
    }

    // 네이버 지도 음식점 크롤링 엔드포인트
    @GetMapping("/fetch-restaurants")
    public String fetchRestaurants() {
        restaurantService.testChromeDriverWithCSSSelector();
        return "Restaurants fetched and saved successfully!";
    }
    //1차 검색어로 가게 찾기
    @PostMapping("/search")
    public List<RestaurantDTO> findByKeyword(@RequestBody Map<String,Object> Keyword){
        String keyword = (String) Keyword.get("searchWord");
        System.out.println(keyword);
        return restaurantService.findByKeyword(keyword);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/restaurants")
    public List<Restaurant> getRestaurants() {
        return restaurantRepository.findAll();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/restaurants/{id}")
    public Restaurant getRestaurantById(@PathVariable Long id) {
        Optional<Restaurant> restaurant = restaurantService.getRestaurantById(id);
        return restaurant.orElse(null);
    }
}
