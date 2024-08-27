//package com.project.zzimccong.controller.s3;
//
//import com.project.zzimccong.service.s3.S3Service;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//@RequestMapping("/s3")
//public class S3Controller {
//
//    private final S3Service s3Service;
//
//    public S3Controller(S3Service s3Service) {
//        this.s3Service = s3Service;
//    }
//
//    // 레스토랑 사진 업로드
//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadRestaurantPhoto(@RequestParam("file") MultipartFile file, @RequestParam("restaurantId") Long restaurantId) {
//        String fileUrl = s3Service.uploadRestaurantPhoto(file, restaurantId);
//        return ResponseEntity.ok(fileUrl);
//    }
//
//    // 레스토랑 사진 삭제
//    @DeleteMapping("/delete-restaurant-photo")
//    public ResponseEntity<Void> deleteRestaurantPhoto(@RequestParam("photoUrl") String photoUrl, @RequestParam("restaurantId") Long restaurantId) {
//        s3Service.deleteRestaurantPhoto(photoUrl, restaurantId);
//        return ResponseEntity.noContent().build();
//    }
//
//    // 레스토랑 사진 URL 조회
//    @GetMapping("/restaurant-photo-url")
//    public ResponseEntity<String> getRestaurantPhotoUrl(@RequestParam("restaurantId") Long restaurantId) {
//        String photoUrl = s3Service.getRestaurantPhotoUrl(restaurantId);
//        return ResponseEntity.ok(photoUrl);
//    }
//}
