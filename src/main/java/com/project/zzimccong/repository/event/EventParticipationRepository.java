package com.project.zzimccong.repository.event;

import com.project.zzimccong.model.entity.coupon.Coupon;
import com.project.zzimccong.model.entity.event.Event;
import com.project.zzimccong.model.entity.event.EventParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventParticipationRepository extends JpaRepository<EventParticipation, Long> {
    // 특정 이벤트에 참여한 모든 기록 조회
    List<EventParticipation> findByEventId(Long eventId);

    // 특정 사용자와 이벤트에 참여한 기록을 조회
    @Query("SELECT p FROM EventParticipation p WHERE p.user.id = :userId AND p.event.id = :eventId")
    List<EventParticipation> findByUserIdAndEventId(@Param("userId") Integer userId, @Param("eventId") Long eventId);

    // 특정 이벤트에서 사용된 모든 쿠폰 조회
    @Query("SELECT ep FROM EventParticipation ep WHERE ep.event.id = :eventId")
    List<Coupon> findCouponsByEventId(@Param("eventId") Long eventId);

    // 특정 이벤트에서 당첨된 참여자 조회
    @Query("SELECT ep FROM EventParticipation ep WHERE ep.event.id = :eventId AND ep.winner = true")
    List<EventParticipation> findWinnersByEventId(@Param("eventId") Long eventId);

    // 특정 이벤트에 대한 모든 참가 기록 삭제
    void deleteByEvent(Event event);

    // 이벤트에 따른 참가 기록을 조회
    List<EventParticipation> findByEvent(Event event);


    // 특정 유저가 특정 이벤트에서 사용한 추첨권 수량 조회
    @Query("SELECT SUM(ep.usedCouponCount) FROM EventParticipation ep WHERE ep.user.id = :userId AND ep.event.id = :eventId")
    Integer findTotalUsedCouponsByUserInEvent(@Param("userId") Integer userId, @Param("eventId") Long eventId);

    // 특정 유저가 모든 이벤트에서 사용한 추첨권 수량 조회
    @Query("SELECT SUM(ep.usedCouponCount) FROM EventParticipation ep WHERE ep.user.id = :userId")
    Integer findTotalUsedCouponsByUserInAllEvents(@Param("userId") Integer userId);
}