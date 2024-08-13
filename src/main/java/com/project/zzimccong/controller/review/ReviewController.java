package com.project.zzimccong.controller.review;

import com.project.zzimccong.model.dto.review.ReviewDTO;
import com.project.zzimccong.model.entity.review.Review;
import com.project.zzimccong.security.jwt.JwtTokenUtil;
import com.project.zzimccong.service.review.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final JwtTokenUtil jwtTokenUtil;

    public ReviewController(ReviewService reviewService, JwtTokenUtil jwtTokenUtil) {
        this.reviewService = reviewService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody ReviewDTO reviewDTO) {
        try {
            Review savedReview = reviewService.saveReview(reviewDTO);
            return ResponseEntity.ok(savedReview);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getUserReviews(@PathVariable Integer userId) {
        System.out.println("Received request for userId: " + userId);
        List<Review> reviews = reviewService.findByUserId(userId);
        System.out.println("Fetched reviews: " + reviews);
        if (reviews.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/corp/{corpId}")
    public ResponseEntity<List<Review>> getCorpReviews(@PathVariable Integer corpId) {
        List<Review> reviews = reviewService.findByCorpId(corpId);
        if (reviews.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reviews);
    }

}
