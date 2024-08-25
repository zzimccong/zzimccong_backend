package com.project.zzimccong.repository.zzim;

import com.project.zzimccong.model.entity.zzim.Zzim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZzimRepository extends JpaRepository<Zzim, Long> {
    List<Zzim> findByUserId(Integer userId);
    List<Zzim> findByRestaurantId(Long restaurantId);
    Optional<Zzim> findByUserIdAndRestaurantId(Integer userId, Long restaurantId);
    Optional<Zzim> findByCorporationIdAndRestaurantId(Integer corpId, Long restaurantId); // 새로운 메소드 추가

    List<Zzim> findByCorporationId(Integer userId);
}
