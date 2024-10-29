package com.project.zzimccong.repository.coupon;

import com.project.zzimccong.model.entity.coupon.Coupon;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {

    @Query("SELECT c.cnt FROM Coupon c WHERE c.user.id = :userId AND c.type = :type")
    Integer findCntByUserIdAndType(@Param("userId") Integer userId, @Param("type") String type);

    @Query("SELECT c FROM Coupon c WHERE c.user.id = :userId AND c.type = '할인권'")
    Coupon findDiscountCouponByUserId(@Param("userId") Integer userId);


    // 특정 타입의 쿠폰 개수를 합산하여 반환
    @Query("SELECT SUM(c.cnt) FROM Coupon c WHERE c.user.id = :userId AND c.type = :type")
    Integer findTotalCntByUserIdAndType(@org.springframework.data.repository.query.Param("userId") Integer userId, @org.springframework.data.repository.query.Param("type") String type);

    // 특정 타입의 모든 쿠폰 객체 조회 (사용여부, 개수, id 등)
    @Query("SELECT c FROM Coupon c WHERE c.user.id = :userId AND c.type = :type AND c.cnt > 0 AND c.used = false")
    List<Coupon> findAllByUserIdAndType(@org.springframework.data.repository.query.Param("userId") Integer userId, @org.springframework.data.repository.query.Param("type") String type);

    // 유저가 가진 사용되지 않은 모든 쿠폰 조회
    @Query("SELECT c FROM Coupon c WHERE c.user.id = :userId AND c.used = false")
    List<Coupon> findAllUnusedByUserId(@org.springframework.data.repository.query.Param("userId") Integer userId);

    // 유저가 특정 이벤트에 사용한 쿠폰 조회
    @Query("SELECT c FROM Coupon c WHERE c.user.id = :userId AND c.event.id = :eventId")
    List<Coupon> findAllByUserIdAndEventId(@org.springframework.data.repository.query.Param("userId") Integer userId, @org.springframework.data.repository.query.Param("eventId") Long eventId);

    // 특정 사용자와 이벤트에 대해 사용된 쿠폰 조회
    List<Coupon> findAllByUserIdAndEventIdAndUsedTrue(Integer userId, Long eventId);
}
