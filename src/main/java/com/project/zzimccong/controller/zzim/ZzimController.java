package com.project.zzimccong.controller.zzim;

import com.project.zzimccong.model.dto.zzim.ZzimDTO;
import com.project.zzimccong.model.entity.zzim.Zzim;
import com.project.zzimccong.service.zzim.ZzimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zzim")
public class ZzimController {

    @Autowired
    private ZzimService zzimService;

    @PostMapping
    public ZzimDTO createZzim(@RequestBody ZzimDTO zzimDTO, @RequestParam String userType) {
        return zzimService.createZzim(zzimDTO, userType);
    }

    @DeleteMapping("/{userId}/{restaurantId}")
    public void deleteZzim(@PathVariable Integer userId, @PathVariable Long restaurantId, @RequestParam String userType) {
        zzimService.deleteZzimByUserIdAndRestaurantId(userId, restaurantId, userType);
    }

//    @GetMapping("/user/{userId}")
//    public List<ZzimDTO> getZzimsByUserId(@PathVariable Integer userId, @RequestParam String userType) {
//        return zzimService.getZzimsByUserId(userId, userType);
//    }

    @GetMapping("/user/{userId}")
    public List<ZzimDTO> getZzimsByUserId(@PathVariable Integer userId, @RequestParam String userType) {
        System.out.println("Received userType: " + userType); // 디버그용 로그
        return zzimService.getZzimsByUserId(userId, userType);
    }

}
