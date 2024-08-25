package com.project.zzimccong.service.zzim;

import com.project.zzimccong.model.dto.zzim.ZzimDTO;
import com.project.zzimccong.model.entity.zzim.Zzim;

import java.util.List;

public interface ZzimService {

    Zzim createZzim(Zzim zzim, String userType);

    void deleteZzimByUserIdAndRestaurantId(Integer userId, Long restaurantId, String userType);

    List<ZzimDTO> getZzimsByUserId(Integer userId, String userType);

    List<Zzim> getZzimsByRestaurantId(Long restaurantId);
}