package com.project.zzimccong.service.event;

import com.project.zzimccong.model.entity.event.Event;
import com.project.zzimccong.model.entity.event.EventParticipation;
import com.project.zzimccong.model.entity.store.Restaurant;
import com.project.zzimccong.repository.event.EventParticipationRepository;
import com.project.zzimccong.repository.event.EventRepository;
import com.project.zzimccong.repository.store.RestaurantRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final RestaurantRepository restaurantRepository;

    private final EventParticipationRepository eventParticipationRepository;

    public EventServiceImpl(EventRepository eventRepository, RestaurantRepository restaurantRepository, EventParticipationRepository eventParticipationRepository) {
        this.eventRepository = eventRepository;
        this.restaurantRepository = restaurantRepository;
        this.eventParticipationRepository = eventParticipationRepository;
    }

    @Override
    public Event createEvent(Event event, Long restaurantId) {
        // 주어진 레스토랑 ID로 레스토랑을 조회
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant with ID: " + restaurantId + " not found"));

        // 이벤트에 레스토랑을 설정하고 이벤트 저장
        event.setRestaurant(restaurant);
        return eventRepository.save(event);
    }

    @Override
    public Event findEventById(Long eventId) {
        // 주어진 이벤트 ID로 이벤트를 조회
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event with ID: " + eventId + " not found"));
    }

    @Override
    public List<Event> findEventsByRestaurantId(Long restaurantId) {
        // 주어진 레스토랑 ID로 모든 이벤트를 조회
        return eventRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public List<Event> findAllEvents() {
        // 모든 이벤트를 조회
        return eventRepository.findAll();
    }

    @Override
    public Event updateEvent(Long eventId, Event eventDetails) {
        // 주어진 이벤트 ID로 이벤트를 조회하고 정보를 업데이트
        Event event = findEventById(eventId);

        if (eventDetails.getStartDate() != null && !eventDetails.getStartDate().equals(event.getStartDate())) {
            event.setStartDate(eventDetails.getStartDate());
        }
        if (eventDetails.getEndDate() != null && !eventDetails.getEndDate().equals(event.getEndDate())) {
            event.setEndDate(eventDetails.getEndDate());
        }

        // 기타 필요한 필드 업데이트...

        return eventRepository.save(event);
    }
}
