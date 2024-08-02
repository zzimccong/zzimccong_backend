package com.project.zzimccong.controller.store;


import com.project.zzimccong.model.dto.store.RestaurantDTO;
import com.project.zzimccong.service.store.RestaurantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search")
public class StoreController {

    RestaurantService restaurantService;

    public StoreController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    //1차 검색어로 가게 찾기
    @PostMapping()
    public List<RestaurantDTO> findByKeyword(@RequestBody Map<String,Object> Keyword){
        String keyword = (String) Keyword.get("searchWord");
        System.out.println(keyword);
        return restaurantService.findByKeyword(keyword);
    }

}
