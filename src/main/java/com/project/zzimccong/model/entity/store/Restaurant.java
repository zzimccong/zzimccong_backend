package com.project.zzimccong.model.entity.store;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.zzimccong.model.entity.cart.Cart;
import com.project.zzimccong.model.entity.event.Event;
import com.project.zzimccong.model.entity.reservation.Reservation;
import com.project.zzimccong.model.entity.timeSlot.TimeSlot;
import com.project.zzimccong.model.entity.user.User;
import com.project.zzimccong.model.entity.zzim.Zzim;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "TB_RESTAURANT")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // 기본키
    private String name; // 음식점 이름
    private String category; // 음식 종류
    private String roadAddress; // 도로명 주소
    private String numberAddress; // 지번 주소
    private String phoneNumber; // 전화 번호
    @Column(length = 1000)
    private String detailInfo; // 상세정보
    @Column(length = 1000)
    private String businessHours; // 영업시간
    private String link; // 링크

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "restaurant-menus")
    private List<Menu> menus; // 메뉴

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "restaurant-zzims")
    private List<Zzim> zzims; // 찜

    @Column(columnDefinition = "TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String facilities; // 편의 시설

    @Column(columnDefinition = "TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String parkingInfo; // 주차 정보

    @Column(length = 1000)
    private String mainPhotoUrl; // 메인 사진
    @Column(length = 1000)
    private String photo1Url; // 사진1
    @Column(length = 1000)
    private String photo2Url; // 사진2
    @Column(length = 1000)
    private String photo3Url; // 사진3
    @Column(length = 1000)
    private String photo4Url; // 사진4
    @Column(length = 1000)
    private String photo5Url; // 사진5

    private double latitude; // 위도
    private double longitude; // 경도
    private String seats;  // 좌석 정보
    private Integer reservationSeats;


    private String state; // 영업 상태

    private String lotteryEvent; // 추첨 상태

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;


    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference  // 고유한 참조 이름 설정
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "restaurant-timeslots")
    private List<TimeSlot> timeSlots;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "restaurant-lists")
    private List<Cart> restaurantLists;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "restaurant-events")
    private List<Event> events; // 이벤트


    public Restaurant() {}

    public Restaurant(long id){
        this.id = id;
    }
    public Restaurant(long id, String name, String category, String roadAddress, String numberAddress, String phoneNumber, String detailInfo, String businessHours, String link, List<Menu> menus, List<Zzim> zzims, String facilities, String parkingInfo, String mainPhotoUrl, String photo1Url, String photo2Url, String photo3Url, String photo4Url, String photo5Url, double latitude, double longitude, String seats, Integer reservationSeats, String state, String lotteryEvent, User user, List<Reservation> reservations, List<TimeSlot> timeSlots, List<Cart> restaurantLists, List<Event> events) {
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
        this.zzims = zzims;
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
        this.reservationSeats = reservationSeats;
        this.state = state;
        this.lotteryEvent = lotteryEvent;
        this.user = user;
        this.reservations = reservations;
        this.timeSlots = timeSlots;
        this.restaurantLists = restaurantLists;
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

    public void setDetailInfo(String detailInfo) {
        this.detailInfo = detailInfo;
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

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public List<Zzim> getZzims() {
        return zzims;
    }

    public void setZzims(List<Zzim> zzims) {
        this.zzims = zzims;
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

    public Integer getReservationSeats() {
        return reservationSeats;
    }

    public void setReservationSeats(Integer reservationSeats) {
        this.reservationSeats = reservationSeats;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLotteryEvent() {
        return lotteryEvent;
    }

    public void setLotteryEvent(String lotteryEvent) {
        this.lotteryEvent = lotteryEvent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public List<Cart> getRestaurantLists() {
        return restaurantLists;
    }

    public void setRestaurantLists(List<Cart> restaurantLists) {
        this.restaurantLists = restaurantLists;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

}
