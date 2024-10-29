package com.project.zzimccong.model.dto.zzim;

import com.project.zzimccong.model.entity.corp.Corporation;
import com.project.zzimccong.model.entity.store.Restaurant;
import com.project.zzimccong.model.entity.user.User;
import com.project.zzimccong.model.entity.zzim.Zzim;
import com.project.zzimccong.repository.corp.CorporationRepository;
import com.project.zzimccong.repository.store.RestaurantRepository;
import com.project.zzimccong.repository.user.UserRepository;

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

    // ZzimDTO를 Zzim 엔티티로 변환하는 메서드
    public Zzim toEntity(UserRepository userRepository, CorporationRepository corporationRepository, RestaurantRepository restaurantRepository) {
        User user = null;
        Corporation corporation = null;
        Restaurant restaurant = restaurantRepository.findById(this.restaurantId).orElse(null);

        if (this.userId != null) {
            user = userRepository.findById(this.userId).orElse(null);
        }

        if (this.corpId != null) {
            corporation = corporationRepository.findById(this.corpId).orElse(null);
        }

        return new Zzim(
                this.id,
                user,
                corporation,
                restaurant,
                this.name
        );
    }
}
