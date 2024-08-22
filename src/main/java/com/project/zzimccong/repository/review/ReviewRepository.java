package com.project.zzimccong.repository.review;

import com.project.zzimccong.model.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByReservation_Restaurant_Id(Long restaurantId);
    Optional<Review> findByReservation_Id(Long reservationId);
    boolean existsByReservation_Id(Long reservationId);
}
