package com.project.zzimccong.repository.reservation;

import com.project.zzimccong.model.entity.reservation.Reservation;
import com.project.zzimccong.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Integer userId);
    List<Reservation> findByCorporationId(Integer corpId);
    List<Reservation> findByRestaurantIdAndReservationTime(Long restaurantId, LocalDateTime reservationTime);

}
