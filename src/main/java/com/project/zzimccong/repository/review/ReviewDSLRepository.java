package com.project.zzimccong.repository.review;

import com.project.zzimccong.model.entity.review.Review;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.zzimccong.model.entity.review.QReview.review;

@Repository
@RequiredArgsConstructor
public class ReviewDSLRepository {

    private final JPAQueryFactory queryFactory;

    public Double getAverageRateByUserRoleAndRestaurant(String role, Long restaurantId) {
        return queryFactory
                .select(review.rate.avg())
                .from(review)
                .where(role.equals("CORP")
                        ? review.reservation.corporation.role.eq(role)
                        : review.reservation.user.role.eq(role)
                        .and(review.reservation.restaurant.id.eq(restaurantId)))
                .fetchOne();
    }

    // 사용자 ID와 역할에 따른 리뷰 조회
    public List<Review> findByUserIdAndRole(Integer userId, String role) {
        return queryFactory
                .selectFrom(review)
                .where(
                        role.equals("USER")
                                ? review.reservation.user.id.eq(userId)
                                : review.reservation.corporation.id.eq(userId) // 'CORP'인 경우 Corporation ID로 검색
                )
                .fetch();
    }
}
