package com.project.zzimccong.service.review;

import com.project.zzimccong.model.dto.review.ReviewDTO;
import com.project.zzimccong.model.entity.reservation.Reservation;
import com.project.zzimccong.model.entity.review.Review;
import com.project.zzimccong.repository.reservation.ReservationRepository;
import com.project.zzimccong.repository.review.ReviewDSLRepository;
import com.project.zzimccong.repository.review.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    private ReviewDSLRepository reviewDSLRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ReservationRepository reservationRepository) {
        this.reviewRepository = reviewRepository;
        this.reservationRepository = reservationRepository;
    }

    private double calculateRate(int taste, int mood, int convenient) {
        return (taste + mood + convenient) / 3.0;
    }

    @Override
    public Review createReview(ReviewDTO reviewDto) {
        // 예약 확인
        Reservation reservation = reservationRepository.findById(reviewDto.getReservationId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation ID"));

        // rate 계산 (taste, mood, convenient의 평균값)
        double rate = calculateRate(reviewDto.getTaste(), reviewDto.getMood(), reviewDto.getConvenient());

        // 소수점 첫 번째 자리에서 반올림
        rate = Math.round(rate * 10) / 10.0;

        // Review 객체 생성
        Review review = new Review();
        review.setReservation(reservation);
        review.setContent(reviewDto.getContent());
        review.setTaste(reviewDto.getTaste());
        review.setMood(reviewDto.getMood());
        review.setConvenient(reviewDto.getConvenient());
        review.setRate(rate);

        return reviewRepository.save(review);
    }

    @Override
    public List<ReviewDTO> getReviewsByUserId(Integer userId, String role) {
        List<Review> reviews = reviewDSLRepository.findByUserIdAndRole(userId, role);
        return reviews.stream().map(review -> {
            // Restaurant 엔터티에서 가게 이름을 가져옴
            String restaurantName = review.getReservation().getRestaurant().getName();
            String userName;
            String corpName;
            if("USER".equals(role)) {
                userName = review.getReservation().getUser().getName();
                corpName=null;
            } else{
                corpName=review.getReservation().getCorporation().getCorpName();
                userName=null;
            }
            return new ReviewDTO(
                    review.getReservation().getId(),
                    review.getContent(),
                    review.getTaste(),
                    review.getMood(),
                    review.getConvenient(),
                    review.getRate(),
                    restaurantName,  // 가게 이름 설정
                    userName,
                    corpName
            );
        }).collect(Collectors.toList());
    }

    @Override
    public List<ReviewDTO> getReviewsByRestaurantId(Long restaurantId) {
        // 해당 레스토랑 ID에 대한 모든 리뷰 조회
        List<Review> reviews = reviewRepository.findByReservation_Restaurant_Id(restaurantId);

        return reviews.stream().map(review -> {
            // Restaurant 엔터티에서 가게 이름을 가져옴
            String restaurantName = review.getReservation().getRestaurant().getName();

            // User와 Corporation 중 하나는 null일 수 있으므로 이를 처리
            String userName = review.getReservation().getUser() != null
                    ? review.getReservation().getUser().getName()
                    : null;  // User가 null이면 null로 처리

            String corpName = review.getReservation().getCorporation() != null
                    ? review.getReservation().getCorporation().getCorpName()
                    : null;  // Corporation이 null이면 null로 처리

            // ReviewDTO에 UserName 또는 CorpName 중 어느 하나가 설정되도록 처리
            return new ReviewDTO(
                    review.getReservation().getId(),
                    review.getContent(),
                    review.getTaste(),
                    review.getMood(),
                    review.getConvenient(),
                    review.getRate(),
                    restaurantName,  // 가게 이름 설정
                    userName,        // userName 설정
                    corpName         // corpName 설정
            );
        }).collect(Collectors.toList());
    }



    @Override
    public void deleteReviewByReservationId(Long reservationId) {
        // 예약 ID에 해당하는 리뷰가 있는지 확인 후 삭제
        Review review = reviewRepository.findByReservation_Id(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));
        reviewRepository.delete(review);
    }

    @Override
    public boolean existsByReservationId(Long reservationId) {
        return reviewRepository.existsByReservation_Id(reservationId);
    }

    @Override
    public Map<String, Double> getAverageRatesByUserRoleAndRestaurant(Long restaurantId) {
        Double userAverage = reviewDSLRepository.getAverageRateByUserRoleAndRestaurant("USER", restaurantId);
        Double corpAverage = reviewDSLRepository.getAverageRateByUserRoleAndRestaurant("CORP", restaurantId);

        Map<String, Double> averages = new HashMap<>();
        averages.put("USER", userAverage != null ? userAverage : 0.0);
        averages.put("CORP", corpAverage != null ? corpAverage : 0.0);

        return averages;
    }
}
