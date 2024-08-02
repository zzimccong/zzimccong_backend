package com.project.zzimccong.model.dto.store;

import com.project.zzimccong.model.entity.store.Restaurant;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor //모든 필드를 매개변수로 받는
@NoArgsConstructor  //매개변수가 없는 기본생성자
@Builder            //빌더패턴 적용한 클래스
@Getter
@Setter
public class RestaurantDTO {

    private long id;
    private String name;
    private String category;
    private String roadAddress; // 도로명 주소
    private String numberAddress; // 지번 주소
    private String phoneNumber; // 전화 번호
    private String detailInfo; // 상세정보
    private String businessHours; // 영업시간 정보를 문자열로 저장
    private String link; // 링크

    private List<MenuDTO> menus;

    private String facilities;
    private String parkingInfo;

    private String photo1Url;
    private String photo2Url;
    private String photo3Url;
    private String photo4Url;
    private String photo5Url;

    private double latitude; // 위도 필드 추가
    private double longitude; // 경도 필드 추가
    private String seats;  // 좌석 정보를 저장할 컬럼 추가

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
                .photo1Url(restaurant.getPhoto1Url())
                .photo2Url(restaurant.getPhoto2Url())
                .photo3Url(restaurant.getPhoto3Url())
                .photo4Url(restaurant.getPhoto4Url())
                .photo5Url(restaurant.getPhoto5Url())
                .latitude(restaurant.getLatitude())
                .longitude(restaurant.getLongitude())
                .seats(restaurant.getSeats())
                .build();
    }

    public static List<RestaurantDTO> toRestaurantDTOList(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(RestaurantDTO::toRestaurantDTO)
                .collect(Collectors.toList());
    }

}
