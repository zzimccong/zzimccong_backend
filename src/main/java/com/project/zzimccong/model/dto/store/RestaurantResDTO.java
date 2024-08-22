package com.project.zzimccong.model.dto.store;

import com.project.zzimccong.model.entity.store.Restaurant;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;
@Builder
public class RestaurantResDTO {

    private long id;
    private String name;
    private String category;
    private String roadAddress;
    private String mainPhotoUrl;
    private String photo1Url;
    private Double grade;

    public RestaurantResDTO() {}

    public RestaurantResDTO(long id, String name, String category, String roadAddress, String mainPhotoUrl, String photo1Url, Double grade) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.roadAddress = roadAddress;
        this.mainPhotoUrl = mainPhotoUrl;
        this.photo1Url = photo1Url;
        this.grade = grade;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getRoadAddress() {
        return roadAddress;
    }

    public String getMainPhotoUrl() {
        return mainPhotoUrl;
    }

    public String getPhoto1Url() {
        return photo1Url;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setRoadAddress(String roadAddress) {
        this.roadAddress = roadAddress;
    }

    public void setMainPhotoUrl(String mainPhotoUrl) {
        this.mainPhotoUrl = mainPhotoUrl;
    }

    public void setPhoto1Url(String photo1Url) {
        this.photo1Url = photo1Url;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }
}
