package com.project.zzimccong.model.dto.store;

import com.project.zzimccong.model.entity.store.Menu;
import com.project.zzimccong.model.entity.store.Restaurant;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor //모든 필드를 매개변수로 받는
@NoArgsConstructor  //매개변수가 없는 기본생성자
@Builder            //빌더패턴 적용한 클래스
@Getter
@Setter
public class MenuDTO {

    private Long id;
    private Restaurant restaurant;
    private String name;
    private String price;
    private String description;
    private String photoUrl; // 메뉴 사진 URL 필드 추가

    // menu --> menuDTO
    private static MenuDTO toMenuDTO(Menu menu) {
        return MenuDTO.builder()
                .id(menu.getId())
                .restaurant(menu.getRestaurant())
                .name(menu.getName())
                .price(menu.getPrice())
                .description(menu.getDescription())
                .photoUrl(menu.getPhotoUrl())
                .build();
    }

    public static List<MenuDTO> toMenuDTO(List<Menu> menuList) {
        return menuList.stream().map(MenuDTO::toMenuDTO).collect(Collectors.toList());
    }
}
