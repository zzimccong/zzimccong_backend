package com.project.zzimccong.model.dto.zzim;

public class ZzimDTO {
    private long id;
    private Integer userId;
    private Integer corpId;
    private long restaurantId;
    private String name;

    public ZzimDTO() {
    }

    public ZzimDTO(long id, Integer userId, Integer corpId, long restaurantId, String name) {
        this.id = id;
        this.userId = userId;
        this.corpId = corpId;
        this.restaurantId = restaurantId;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCorpId() {
        return corpId;
    }

    public void setCorpId(Integer corpId) {
        this.corpId = corpId;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
