package com.project.zzimccong.model.dto.store;

import com.project.zzimccong.model.entity.store.Restaurant;

public class MenuDTO {

    private Long id; // 기본키
    private Restaurant restaurant; // 레스토랑
    private String name; // 메뉴 이름
    private String price; // 메뉴 가격
    private String description; // 메뉴 설명
    private String photoUrl; // 메뉴 사진

    public MenuDTO() {}

    public MenuDTO(Long id, Restaurant restaurant, String name, String price, String description, String photoUrl) {
        this.id = id;
        this.restaurant = restaurant;
        this.name = name;
        this.price = price;
        this.description = description;
        this.photoUrl = photoUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public String toString() {
        return "MenuDTO{" +
                "id=" + id +
                ", restaurant=" + restaurant +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}
