package com.project.zzimccong.service.event;

import com.project.zzimccong.model.entity.event.Event;

import java.util.List;

public interface EventService {

    Event createEvent(Event event, Long restaurantId); // 이벤트 생성


}
