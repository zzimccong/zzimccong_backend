package com.project.zzimccong.model.dto.event;

import com.project.zzimccong.model.entity.event.Event;
import com.project.zzimccong.model.entity.store.Restaurant;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class EventDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long restaurantId;
    private String photo1Url; // 가게 사진
    private String restaurantName;  // 가게 이름
    private String category; // 가게 품목
    private String detailInfo;  // 가게 설명
    private String roadAddress;  // 가게 주소
    private String status;

    public EventDTO() {
    }

    @Builder
    public EventDTO(Long id,  LocalDate startDate, LocalDate endDate, Long restaurantId, String status, String photo1Url, String restaurantName, String category, String detailInfo, String roadAddress) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.restaurantId = restaurantId;
        this.status = status;
        this.photo1Url = photo1Url;
        this.restaurantName = restaurantName;
        this.category = category;
        this.detailInfo = detailInfo;
        this.roadAddress = roadAddress;
    }

    public static EventDTO toEventDTO(Event event) {
        return buildFromEntity(event);
    }

    private static EventDTO buildFromEntity(Event event) {
        Restaurant restaurant = event.getRestaurant();
        return EventDTO.builder()
                .id(event.getId())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .restaurantId(restaurant.getId())
                .status(event.getStatus())
                .photo1Url(restaurant.getPhoto1Url())
                .restaurantName(restaurant.getName())
                .category(restaurant.getCategory())
                .detailInfo(restaurant.getDetailInfo())
                .roadAddress(restaurant.getRoadAddress())
                .build();
    }

    public static List<EventDTO> toEventDTOList(List<Event> events) {
        return events.stream()
                .map(EventDTO::toEventDTO)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getPhoto1Url() {
        return photo1Url;
    }

    public void setPhoto1Urll(String photo1Url) {
        this.photo1Url = photo1Url;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDetailInfo() {
        return detailInfo;
    }

    public void setDetailInfo(String detailInfo) {
        this.detailInfo = detailInfo;
    }

    public String getRoadAddress() {
        return roadAddress;
    }

    public void setRoadAddress(String roadAddress) {
        this.roadAddress = roadAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static EventDTO fromEntity(Event event) {
        return buildFromEntity(event);
    }

    public static Event toEntity(EventDTO eventDTO, Restaurant restaurant) {
        Event event = new Event();
        event.setId(eventDTO.getId());
        event.setStartDate(eventDTO.getStartDate());
        event.setEndDate(eventDTO.getEndDate());
        event.setRestaurant(restaurant);
        event.setStatus(eventDTO.getStatus());
        return event;
    }

    @Override
    public String toString() {
        return "EventDTO{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", restaurantId=" + restaurantId +
                ", photo1Url='" + photo1Url + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", category='" + category + '\'' +
                ", detailInfo='" + detailInfo + '\'' +
                ", roadAddress='" + roadAddress + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
