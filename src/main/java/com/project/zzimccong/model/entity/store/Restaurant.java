package com.project.zzimccong.model.entity.store;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.zzimccong.model.dto.store.RestaurantDTO;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor //모든 필드를 매개변수로 받는
@NoArgsConstructor  //매개변수가 없는 기본생성자
@Builder            //빌더패턴 적용한 클래스
@Getter
@Setter
@ToString
@Table(name = "restaurant")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String category;
    private String roadAddress; // 도로명 주소
    private String numberAddress; // 지번 주소
    private String phoneNumber; // 전화 번호
    @Column(length = 1000)
    private String detailInfo; // 상세정보
    @Column(length = 1000)
    private String businessHours; // 영업시간 정보를 문자열로 저장
    private String link; // 링크

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Menu> menus;

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




}
