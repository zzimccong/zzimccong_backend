package com.project.zzimccong.controller.store;


import com.project.zzimccong.model.dto.store.RestaurantDTO;
import com.project.zzimccong.model.dto.store.RestaurantResDTO;
import com.project.zzimccong.model.entity.store.Restaurant;
import com.project.zzimccong.repository.store.RestaurantRepository;
import com.project.zzimccong.repository.user.UserRepository;
import com.project.zzimccong.service.s3.S3Service;
import com.project.zzimccong.service.store.RestaurantService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

//@Controller
@RestController
@RequestMapping("/api")
public class RestaurantController {

    private final UserRepository userRepository;
    private RestaurantService restaurantService;
    private RestaurantRepository restaurantRepository;
    private S3Service s3Service;


    @Value("${naver.client.id}")
    private String naverClientId;

    @Value("${naver.client.secret}")
    private String naverClientSecret;

    public RestaurantController(RestaurantService restaurantService, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.restaurantService = restaurantService;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;

    }

    // 네이버 지도 음식점 크롤링 엔드포인트
    @GetMapping("/fetch-multiple-restaurants")
    public String fetchMultipleRestaurants() {
        String[] urls = {
                "https://map.naver.com/p/search/%EB%B6%80%EC%82%B0%20%EC%9D%8C%EC%8B%9D%EC%A0%90?c=10.00,0,0,0,dh",
                "https://map.naver.com/p/search/%EC%84%9C%EC%9A%B8%20%EC%9D%8C%EC%8B%9D%EC%A0%90?c=10.00,0,0,0,dh",
                "https://map.naver.com/p/search/%EB%8C%80%EA%B5%AC%20%EC%9D%8C%EC%8B%9D%EC%A0%90?c=10.00,0,0,0,dh"
        };

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (String url : urls) {
            futures.add(CompletableFuture.runAsync(() -> {
                restaurantService.crawlRestaurants(url);
            }));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        return "Multiple restaurants fetched and saved successfully!";
    }
    //1차 검색어로 가게 찾기
    @GetMapping("/search/{searchWord}")
    public List<RestaurantResDTO> findByKeyword(@PathVariable String searchWord){
        List<RestaurantResDTO> results = restaurantService.findByKeyword(searchWord);
        return results.size() > 20 ? results.subList(0, 20) : results;
    }

    //2차 키워드로 가게 필터
    @PostMapping("/search/filter")
    public List<RestaurantResDTO> findByFilter(@RequestBody Map<String, Object> filters){
        List<RestaurantResDTO> results = restaurantService.findByFilter(filters);
        return results.size() > 20 ? results.subList(0, 20) : results;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/restaurants")
    public List<Restaurant> getRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return restaurants.size() > 50 ? restaurants.subList(0, 50) : restaurants;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/restaurant/{id}")
    public Restaurant getRestaurantById(@PathVariable Long id) {
        Optional<Restaurant> restaurant = restaurantService.getRestaurantById(id);
        return restaurant.orElse(null);
    }

    // 남은 좌석 수를 반환하는 엔드포인트
    @GetMapping("/restaurant/{restaurantId}/availability")
    public ResponseEntity<Map<String, Object>> getAvailableSeats(
            @PathVariable Long restaurantId,
            @RequestParam String date,
            @RequestParam String time) {

        // 요청받은 날짜와 시간을 LocalDateTime으로 변환
        LocalDateTime reservationTime = LocalDateTime.parse(date + "T" + time);

        // 서비스에서 남은 좌석 수를 계산
        int availableSeats = restaurantService.getAvailableSeats(restaurantId, reservationTime);

        Map<String, Object> response = Map.of(
                "restaurantId", restaurantId,
                "reservationTime", reservationTime,
                "availableSeats", availableSeats
        );

        return ResponseEntity.ok(response);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/restaurants/user/{user_id}")
    public List<Restaurant> getRestaurantsByUserId(@PathVariable Integer user_id) {
        return restaurantService.getRestaurantsByUserId(user_id);
    }


    @PostMapping("/restaurant/create-with-photos")
    public ResponseEntity<Restaurant> createRestaurantWithPhotos(
            @RequestPart("restaurant") Restaurant restaurant,
            @RequestPart("photos") MultipartFile[] photos) {

        // 1. 가게 정보 생성
        restaurant.setState("승인 대기 중");
        restaurant.setReservationSeats(20);
        Restaurant createdRestaurant = restaurantService.createRestaurant(restaurant);

        if (createdRestaurant == null) {
            throw new RuntimeException("Failed to create restaurant");
        }

        Long restaurantId = createdRestaurant.getId();

        if (restaurantId == null) {
            throw new RuntimeException("Restaurant ID is null after creation");
        }

        // 2. 사진 업로드
        for (int i = 0; i < photos.length; i++) {
            MultipartFile photo = photos[i];
            if (photo != null && !photo.isEmpty()) {
                String photoUrl = s3Service.uploadRestaurantPhoto(photo, restaurantId);

                // 각 사진 URL을 레스토랑 엔티티에 저장 (예시: photo1Url, photo2Url 등)
                switch (i) {
                    case 0:
                        createdRestaurant.setPhoto1Url(photoUrl);
                        break;
                    case 1:
                        createdRestaurant.setPhoto2Url(photoUrl);
                        break;
                    case 2:
                        createdRestaurant.setPhoto3Url(photoUrl);
                        break;
                    case 3:
                        createdRestaurant.setPhoto4Url(photoUrl);
                        break;
                    case 4:
                        createdRestaurant.setPhoto5Url(photoUrl);
                        break;
                }
            } else {
                throw new RuntimeException("Photo is null or empty");
            }
        }

        // 3. 데이터베이스에 저장
        restaurantRepository.save(createdRestaurant);

        // 4. 생성된 가게 정보를 반환
        return ResponseEntity.ok(createdRestaurant);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/restaurantUpdate/{id}")
    public Restaurant updateRestaurant(@PathVariable Long id, @RequestBody Restaurant restaurantDetails) {
        return restaurantService.updateRestaurant(id, restaurantDetails);
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

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/restaurant/{id}/state")
    public Restaurant updateRestaurantState(@PathVariable Long id, @RequestBody Map<String, String> updates) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);
        if (!optionalRestaurant.isPresent()) {
            throw new RuntimeException("Restaurant not found with id: " + id);
        }

        Restaurant restaurant = optionalRestaurant.get();

        if (updates.containsKey("state")) {
            restaurant.setState(updates.get("state"));
        } else {
            throw new IllegalArgumentException("State field is required");
        }

        return restaurantRepository.save(restaurant);
    }
}
