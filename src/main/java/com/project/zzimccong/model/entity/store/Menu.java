package com.project.zzimccong.model.entity.store;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.zzimccong.model.dto.store.MenuDTO;
import lombok.*;

import javax.persistence.*;


@Entity
@AllArgsConstructor //모든 필드를 매개변수로 받는
@NoArgsConstructor  //매개변수가 없는 기본생성자
@Builder            //빌더패턴 적용한 클래스
@Getter
@Setter
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @JsonBackReference
    private Restaurant restaurant;

    private String name;
    private String price;
    private String description;
    @Column(length = 1000)
    private String photoUrl; // 메뉴 사진 URL 필드 추가

}
