package com.project.zzimccong.controller.event;

import com.project.zzimccong.model.dto.event.EventDTO;
import com.project.zzimccong.model.entity.event.Event;
import com.project.zzimccong.model.entity.store.Restaurant;
import com.project.zzimccong.service.event.EventService;
import com.project.zzimccong.service.store.RestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private final RestaurantService restaurantService;


    public EventController(EventService eventService, RestaurantService restaurantService) {
        this.eventService = eventService;
        this.restaurantService = restaurantService;
    }

    // 이벤트 생성
    @PostMapping("/create")
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) {
        Restaurant restaurant = restaurantService.findById(eventDTO.getRestaurantId());
        Event event = EventDTO.toEntity(eventDTO, restaurant);
        Event createdEvent = eventService.createEvent(event, eventDTO.getRestaurantId());
        return ResponseEntity.ok(EventDTO.fromEntity(createdEvent));
    }


}
