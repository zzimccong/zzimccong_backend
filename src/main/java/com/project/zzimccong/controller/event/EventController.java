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

    // 특정 id 가진 이벤트 조회
    @GetMapping("/{eventId}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long eventId) {
        Event event = eventService.findEventById(eventId);
        return ResponseEntity.ok(EventDTO.fromEntity(event));
    }

    // 특정 가게에서 열린 모든 이벤트 조회
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<EventDTO>> getEventsByRestaurantId(@PathVariable Long restaurantId) {
        List<Event> events = eventService.findEventsByRestaurantId(restaurantId);
        return ResponseEntity.ok(EventDTO.toEventDTOList(events));
    }

    // 모든 가게에서 열린 이벤트 조회
    @GetMapping("/all")
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        List<Event> events = eventService.findAllEvents();
        return ResponseEntity.ok(EventDTO.toEventDTOList(events));
    }

    // 특정 이벤트 업데이트
    @PutMapping("/{eventId}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long eventId, @RequestBody EventDTO eventDTO) {
        Restaurant restaurant = restaurantService.findById(eventDTO.getRestaurantId());
        Event eventDetails = EventDTO.toEntity(eventDTO, restaurant);
        Event updatedEvent = eventService.updateEvent(eventId, eventDetails);
        return ResponseEntity.ok(EventDTO.fromEntity(updatedEvent));
    }

    // 특정 이벤트 삭제
    @DeleteMapping("/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.ok("이벤트 삭제가 완료되었습니다.");
    }
}
