package com.project.zzimccong.service.zzim;

import com.project.zzimccong.model.dto.zzim.ZzimDTO;
import com.project.zzimccong.model.entity.zzim.Zzim;
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

    @Override
    public Zzim createZzim(Zzim zzim, String userType) {
        if ("USER".equalsIgnoreCase(userType) || "MANAGER".equalsIgnoreCase(userType)) {
            zzim.setUser(zzim.getUser());
            zzim.setCorporation(null);
        } else if ("CORP".equalsIgnoreCase(userType)) {
            zzim.setUser(null);
            zzim.setCorporation(zzim.getCorporation());
        } else {
            throw new IllegalArgumentException("Invalid user type: " + userType);
        }

        return zzimRepository.save(zzim);
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
        System.out.println("Fetching Zzims for userId: " + userId + " with userType: " + userType);
        List<Zzim> zzims;
        if ("CORP".equalsIgnoreCase(userType)) {
            zzims = zzimRepository.findByCorporationId(userId);
        } else {
            zzims = zzimRepository.findByUserId(userId);
        }
        System.out.println("Fetched Zzims: " + zzims);
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


