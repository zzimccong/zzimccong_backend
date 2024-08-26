package com.project.zzimccong.repository.event;

import com.project.zzimccong.model.entity.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // 특정 가게의 특정 이벤트를 조회
    Event findByIdAndRestaurantId(Long eventId, Long restaurantId);

    // 특정 가게에서 열린 모든 이벤트를 조회
    List<Event> findByRestaurantId(Long restaurantId);

    // 모든 가게에서 열린 모든 이벤트를 조회 (JpaRepository 기본 제공)
    List<Event> findAll();
}
