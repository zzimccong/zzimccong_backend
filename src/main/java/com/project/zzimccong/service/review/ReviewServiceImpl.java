package com.project.zzimccong.service.review;

import com.project.zzimccong.model.dto.review.ReviewDTO;
import com.project.zzimccong.model.entity.reservation.Reservation;
import com.project.zzimccong.model.entity.review.Review;
import com.project.zzimccong.repository.reservation.ReservationRepository;
import com.project.zzimccong.repository.review.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ReservationRepository reservationRepository) {
        this.reviewRepository = reviewRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Review saveReview(ReviewDTO reviewDTO) {
        Reservation reservation = reservationRepository.findById(reviewDTO.getReservationId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation ID"));

        Review review = new Review();
        review.setReservation(reservation);
        review.setContent(reviewDTO.getContent());
        review.setTaste(reviewDTO.getTaste());
        review.setMood(reviewDTO.getMood());
        review.setConvenient(reviewDTO.getConvenient());
        review.setRate(reviewDTO.getRate());
        review.setUserId(reviewDTO.getUserId());
        review.setCorpId(reviewDTO.getCorpId());

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> findByUserId(Integer userId) {
        return reviewRepository.findByUserId(userId);
    }

    @Override
    public List<Review> findByCorpId(Integer corpId) {
        return reviewRepository.findByCorpId(corpId);
    }
}
