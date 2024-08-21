package com.project.zzimccong.controller.cart;

import com.project.zzimccong.model.dto.cart.CartDTO;
import com.project.zzimccong.model.dto.cart.CartResDTO;
import com.project.zzimccong.service.reservation.RestaurantCartService;
import com.project.zzimccong.service.store.RestaurantService;
import com.project.zzimccong.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/cart")
public class CartController {

    private final UserService userService;
    private final RestaurantCartService restaurantCartService;
    private final RestaurantService restaurantService;

    public CartController(UserService userService, RestaurantCartService restaurantCartService, RestaurantService restaurantService) {
        this.userService = userService;
        this.restaurantCartService = restaurantCartService;
        this.restaurantService = restaurantService;
    }

    //장바구니추가
    @PostMapping("/add")
    public ResponseEntity<?> saveRestaurantList(@RequestBody CartDTO cartDto){
        restaurantCartService.saveRestaurantList(cartDto);
        return ResponseEntity.ok("장바구니에 추가되었습니다.");
    }


    //장바구니 보여주기
    @GetMapping("/{user_id}")
    public List<CartResDTO> findByUserId(@PathVariable Integer user_id){
        return restaurantCartService.findByUserIdWithRestaurant(user_id);
    }

    //장바구니 삭제
    @PostMapping("/delete")
    public ResponseEntity<?> deleteByStoreId(@RequestBody Map<String, Object> deleteData){
        Integer userId = ((Number) deleteData.get("userId")).intValue();
        List<Integer> storeIds = (List<Integer>) deleteData.get("storeIds");

        restaurantCartService.deleteByStoreId(userId, storeIds);

        return ResponseEntity.ok("삭제 성공");
    }
}
