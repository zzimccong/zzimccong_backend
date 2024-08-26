package com.project.zzimccong.service.event;

import com.project.zzimccong.model.entity.event.Event;

import java.util.List;

public interface EventService {

    Event createEvent(Event event, Long restaurantId); // 이벤트 생성

    Event findEventById(Long eventId); // 특정 id 가진 이벤트 조회

    List<Event> findEventsByRestaurantId(Long restaurantId);  // 특정 가게에서 열린 모든 이벤트 조회

    List<Event> findAllEvents();  // 모든 이벤트를 조회하는 메서드

    Event updateEvent(Long eventId, Event eventDetails); // 이벤트 업데이트

    void deleteEvent(Long eventId); // 이벤트 삭제
}
