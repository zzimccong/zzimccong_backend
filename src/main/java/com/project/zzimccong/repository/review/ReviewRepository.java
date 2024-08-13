package com.project.zzimccong.repository.review;

import com.project.zzimccong.model.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUserId(Integer userId);
    List<Review> findByCorpId(Integer corpId);
}
