package com.project.zzimccong.service.review;

import com.project.zzimccong.model.dto.review.ReviewDTO;
import com.project.zzimccong.model.entity.review.Review;

import java.util.List;

public interface ReviewService {
    Review saveReview(ReviewDTO reviewDTO);
    List<Review> findByUserId(Integer userId);
    List<Review> findByCorpId(Integer corpId);
}
