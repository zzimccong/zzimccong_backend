package com.project.zzimccong.service.zzim;

import com.project.zzimccong.model.dto.zzim.ZzimDTO;
import com.project.zzimccong.model.entity.corp.Corporation;
import com.project.zzimccong.model.entity.store.Restaurant;
import com.project.zzimccong.model.entity.user.User;
import com.project.zzimccong.model.entity.zzim.Zzim;
import com.project.zzimccong.repository.corp.CorporationRepository;
import com.project.zzimccong.repository.store.RestaurantRepository;
import com.project.zzimccong.repository.user.UserRepository;
import com.project.zzimccong.repository.zzim.ZzimRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ZzimServiceImpl implements ZzimService {

    @Autowired
    private ZzimRepository zzimRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CorporationRepository corporationRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public ZzimDTO createZzim(ZzimDTO zzimDTO, String userType) {
        // 레스토랑 ID로 레스토랑을 조회
        Restaurant restaurant = restaurantRepository.findById(zzimDTO.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid restaurant ID"));

        Zzim zzim = zzimDTO.toEntity(userRepository, corporationRepository, restaurantRepository);

        if ("USER".equalsIgnoreCase(userType) || "MANAGER".equalsIgnoreCase(userType)) {
            zzim.setCorporation(null);  // USER 타입이면 corporation은 null로 설정
        } else if ("CORP".equalsIgnoreCase(userType)) {
            zzim.setUser(null);  // CORP 타입이면 user는 null로 설정
        } else {
            throw new IllegalArgumentException("Invalid user type: " + userType);
        }

        zzim.setRestaurant(restaurant);  // 확인된 레스토랑 설정

        Zzim savedZzim = zzimRepository.save(zzim);
        return convertToDTO(savedZzim);
    }

    @Override
    public void deleteZzimByUserIdAndRestaurantId(Integer userId, Long restaurantId, String userType) {
        if ("CORP".equalsIgnoreCase(userType)) {
            Optional<Zzim> zzim = zzimRepository.findByCorporationIdAndRestaurantId(userId, restaurantId);
            zzim.ifPresent(zzimRepository::delete);
        } else {
            Optional<Zzim> zzim = zzimRepository.findByUserIdAndRestaurantId(userId, restaurantId);
            zzim.ifPresent(zzimRepository::delete);
        }
    }

    @Override
    public List<ZzimDTO> getZzimsByUserId(Integer userId, String userType) {
        List<Zzim> zzims;
        if ("CORP".equalsIgnoreCase(userType)) {
            zzims = zzimRepository.findByCorporationId(userId);
        } else {
            zzims = zzimRepository.findByUserId(userId);
        }
        return zzims.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<Zzim> getZzimsByRestaurantId(Long restaurantId) {
        return zzimRepository.findByRestaurantId(restaurantId);
    }


    private ZzimDTO convertToDTO(Zzim zzim) {
        return new ZzimDTO(
                zzim.getId(),
                zzim.getUser() != null ? zzim.getUser().getId() : null,
                zzim.getCorporation() != null ? zzim.getCorporation().getId() : null,
                zzim.getRestaurant().getId(),
                zzim.getName()
        );
    }
}
