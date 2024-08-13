package com.project.zzimccong.controller.store;


import com.project.zzimccong.model.dto.store.RestaurantDTO;
import com.project.zzimccong.model.entity.store.Restaurant;
import com.project.zzimccong.repository.store.RestaurantRepository;
import com.project.zzimccong.service.store.RestaurantService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

//@Controller
@RestController
@RequestMapping("/api")
public class RestaurantController {

    private RestaurantService restaurantService;
    private RestaurantRepository restaurantRepository;

    @Value("${naver.client.id}")
    private String naverClientId;

    @Value("${naver.client.secret}")
    private String naverClientSecret;

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
    @GetMapping("/restaurant/{id}")
    public Restaurant getRestaurantById(@PathVariable Long id) {
        Optional<Restaurant> restaurant = restaurantService.getRestaurantById(id);
        return restaurant.orElse(null);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/restaurantCreate")
    public Restaurant createRestaurant(@RequestBody Restaurant restaurant) {
        restaurant.setState("승인 대기 중");
        return restaurantService.createRestaurant(restaurant);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/geocode")
    public Map<String, Object> getGeocode(@RequestParam String query) {
        String url = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + query;
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", naverClientId);
        headers.set("X-NCP-APIGW-API-KEY", naverClientSecret);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        return response.getBody();
    }
}
