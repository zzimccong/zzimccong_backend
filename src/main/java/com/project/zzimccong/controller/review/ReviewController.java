package com.project.zzimccong.controller.review;

import com.project.zzimccong.model.dto.review.ReviewDTO;
import com.project.zzimccong.model.entity.review.Review;
import com.project.zzimccong.security.jwt.JwtTokenUtil;
import com.project.zzimccong.service.review.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody ReviewDTO reviewDto) {
        Review createdReview = reviewService.createReview(reviewDto);
        return ResponseEntity.ok(createdReview);
    }

    @GetMapping("/myreviews")
    public List<ReviewDTO> getMyReviews(@RequestParam Integer userId, String role) {
        return reviewService.getReviewsByUserId(userId, role);
    }

    // 특정 가게의 리뷰 조회
    @GetMapping("/restaurant")
    public List<ReviewDTO> getReviewsByRestaurantId(@RequestParam Long restaurantId) {
        return reviewService.getReviewsByRestaurantId(restaurantId);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reservationId) {
        reviewService.deleteReviewByReservationId(reservationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<Map<String, Boolean>> checkReviewExists(@PathVariable Long reservationId) {
        boolean reviewExists = reviewService.existsByReservationId(reservationId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("reviewExists", reviewExists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/average-rates")
    public ResponseEntity<Map<String, Double>> getAverageRatesByUserRole(@RequestParam Long restaurantId) {
        Map<String, Double> averages = reviewService.getAverageRatesByUserRoleAndRestaurant(restaurantId);
        return ResponseEntity.ok(averages);
    }
}
