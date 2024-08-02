package com.project.zzimccong.service.store;


import com.project.zzimccong.model.dto.store.RestaurantDTO;
import com.project.zzimccong.model.entity.store.Restaurant;
import com.project.zzimccong.repository.store.RestaurantDSLRepository;
import com.project.zzimccong.repository.store.RestaurantRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantDSLRepository restaurantDSLRepository;
    RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, RestaurantDSLRepository restaurantDSLRepository) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantDSLRepository = restaurantDSLRepository;
    }

    @Override
    public List<RestaurantDTO> findByKeyword(String keyword) {

        List<Restaurant> restaurant = restaurantDSLRepository.findByKeyword(keyword);
        List<RestaurantDTO> dtoList = RestaurantDTO.toRestaurantDTOList(restaurant);

        return dtoList;
    }


}
