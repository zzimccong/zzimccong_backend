package com.project.zzimccong.service.event;

import com.project.zzimccong.model.entity.coupon.Coupon;
import com.project.zzimccong.model.entity.event.EventParticipation;

import java.util.List;
import java.util.Map;

public interface EventParticipationService {
    // 유저가 특정 이벤트에 참여하면서 쿠폰을 사용하는 메서드
    EventParticipation participateInEvent(Integer userId, Long eventId, int couponCount);

    // 특정 이벤트에 참여한 모든 기록을 조회하는 메서드
    List<EventParticipation> getParticipationsByEventId(Long eventId);


    // 특정 유저가 특정 이벤트에 참여한 기록을 조회하는 메서드
    List<EventParticipation> getParticipationsByUserIdAndEventId(Integer userId, Long eventId);

    List<String> getParticipantNamesByEventId(Long eventId);

    // 특정 이벤트에서 사용된 모든 쿠폰을 조회하는 메서드
    List<Coupon> getCouponsUsedInEvent(Long eventId);

    // 특정 이벤트에서 추첨하는 메서드
    Map<String, Object> drawLottery(Long eventId);

    // 사용자가 특정 이벤트에서 사용한 추첨권 수 조회
    Integer getTotalCouponsUsedByUserInEvent(Integer userId, Long eventId);

    // 사용자가 모든 이벤트에서 사용한 총 추첨권 수 조회
    Integer getTotalCouponsUsedByUserInAllEvents(Integer userId);

}
