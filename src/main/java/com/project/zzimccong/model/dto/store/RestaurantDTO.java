package com.project.zzimccong.model.dto.store;

import com.project.zzimccong.model.dto.event.EventDTO;
import com.project.zzimccong.model.entity.store.Restaurant;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

public class RestaurantDTO {

    private long id;
    private String name;
    private String category;
    private String roadAddress;
    private String numberAddress;
    private String phoneNumber;
    private String detailInfo;
    private String businessHours;
    private String link; // 링크

    private List<MenuDTO> menus;

    private String facilities;
    private String parkingInfo;
    private String mainPhotoUrl;
    private String photo1Url;
    private String photo2Url;
    private String photo3Url;
    private String photo4Url;
    private String photo5Url;
    private double latitude;
    private double longitude;
    private String seats;

    private List<EventDTO> events;

    public RestaurantDTO() {}
    @Builder
    public RestaurantDTO(long id, String name, String category, String roadAddress, String numberAddress, String phoneNumber, String detailInfo, String businessHours, String link, List<MenuDTO> menus, String facilities, String parkingInfo, String mainPhotoUrl, String photo1Url, String photo2Url, String photo3Url, String photo4Url, String photo5Url, double latitude, double longitude, String seats, List<EventDTO> events) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.roadAddress = roadAddress;
        this.numberAddress = numberAddress;
        this.phoneNumber = phoneNumber;
        this.detailInfo = detailInfo;
        this.businessHours = businessHours;
        this.link = link;
        this.menus = menus;
        this.facilities = facilities;
        this.parkingInfo = parkingInfo;
        this.mainPhotoUrl = mainPhotoUrl;
        this.photo1Url = photo1Url;
        this.photo2Url = photo2Url;
        this.photo3Url = photo3Url;
        this.photo4Url = photo4Url;
        this.photo5Url = photo5Url;
        this.latitude = latitude;
        this.longitude = longitude;
        this.seats = seats;
        this.events = events;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRoadAddress() {
        return roadAddress;
    }

    public void setRoadAddress(String roadAddress) {
        this.roadAddress = roadAddress;
    }

    public String getNumberAddress() {
        return numberAddress;
    }

    public void setNumberAddress(String numberAddress) {
        this.numberAddress = numberAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDetailInfo() {
        return detailInfo;
    }
    
    public static RestaurantDTO toRestaurantDTO(Restaurant restaurant) {
        return RestaurantDTO.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .category(restaurant.getCategory())
                .roadAddress(restaurant.getRoadAddress())
                .numberAddress(restaurant.getNumberAddress())
                .phoneNumber(restaurant.getPhoneNumber())
                .detailInfo(restaurant.getDetailInfo())
                .businessHours(restaurant.getBusinessHours())
                .link(restaurant.getLink())
                .menus(MenuDTO.toMenuDTO(restaurant.getMenus())) //타입 변환
                .facilities(restaurant.getFacilities())
                .parkingInfo(restaurant.getParkingInfo())
                .mainPhotoUrl(restaurant.getMainPhotoUrl())
                .photo1Url(restaurant.getPhoto1Url())
                .photo2Url(restaurant.getPhoto2Url())
                .photo3Url(restaurant.getPhoto3Url())
                .photo4Url(restaurant.getPhoto4Url())
                .photo5Url(restaurant.getPhoto5Url())
                .latitude(restaurant.getLatitude())
                .longitude(restaurant.getLongitude())
                .seats(restaurant.getSeats())
                .events(EventDTO.toEventDTOList(restaurant.getEvents())) // 이벤트 리스트를 DTO로 변환
                .build();
    }

    public void setDetailInfo(String detailInfo) {
        this.detailInfo = detailInfo;
    }
    public static List<RestaurantDTO> toRestaurantDTOList(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(RestaurantDTO::toRestaurantDTO)
                .collect(Collectors.toList());
    }

    public String getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<MenuDTO> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuDTO> menus) {
        this.menus = menus;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    public String getParkingInfo() {
        return parkingInfo;
    }

    public void setParkingInfo(String parkingInfo) {
        this.parkingInfo = parkingInfo;
    }

    public String getMainPhotoUrl() {
        return mainPhotoUrl;
    }

    public void setMainPhotoUrl(String mainPhotoUrl) {
        this.mainPhotoUrl = mainPhotoUrl;
    }

    public String getPhoto1Url() {
        return photo1Url;
    }

    public void setPhoto1Url(String photo1Url) {
        this.photo1Url = photo1Url;
    }

    public String getPhoto2Url() {
        return photo2Url;
    }

    public void setPhoto2Url(String photo2Url) {
        this.photo2Url = photo2Url;
    }

    public String getPhoto3Url() {
        return photo3Url;
    }

    public void setPhoto3Url(String photo3Url) {
        this.photo3Url = photo3Url;
    }

    public String getPhoto4Url() {
        return photo4Url;
    }

    public void setPhoto4Url(String photo4Url) {
        this.photo4Url = photo4Url;
    }

    public String getPhoto5Url() {
        return photo5Url;
    }

    public void setPhoto5Url(String photo5Url) {
        this.photo5Url = photo5Url;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public List<EventDTO> getEvents() {
        return events;
    }

    public void setEvents(List<EventDTO> events) {
        this.events = events;
    }


    @Override
    public String toString() {
        return "RestaurantDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", roadAddress='" + roadAddress + '\'' +
                ", numberAddress='" + numberAddress + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", detailInfo='" + detailInfo + '\'' +
                ", businessHours='" + businessHours + '\'' +
                ", link='" + link + '\'' +
                ", menus=" + menus +
                ", facilities='" + facilities + '\'' +
                ", parkingInfo='" + parkingInfo + '\'' +
                ", mainPhotoUrl='" + mainPhotoUrl + '\'' +
                ", photo1Url='" + photo1Url + '\'' +
                ", photo2Url='" + photo2Url + '\'' +
                ", photo3Url='" + photo3Url + '\'' +
                ", photo4Url='" + photo4Url + '\'' +
                ", photo5Url='" + photo5Url + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", seats='" + seats + '\'' +
                ", events=" + events +
                '}';
    }
}
