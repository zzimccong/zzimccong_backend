package com.project.zzimccong.service.review;

import com.project.zzimccong.model.dto.review.ReviewDTO;
import com.project.zzimccong.model.entity.review.Review;

import java.util.List;
import java.util.Map;

public interface ReviewService {
    Review createReview(ReviewDTO reviewDto);
    List<ReviewDTO> getReviewsByUserId(Integer userId, String role);
    List<ReviewDTO> getReviewsByRestaurantId(Long restaurantId);
    void deleteReviewByReservationId(Long reservationId);
    boolean existsByReservationId(Long reservationId);
    Map<String, Double> getAverageRatesByUserRoleAndRestaurant(Long restaurantId);
}
