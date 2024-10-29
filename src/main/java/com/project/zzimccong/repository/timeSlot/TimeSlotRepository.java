package com.project.zzimccong.repository.timeSlot;

import com.project.zzimccong.model.entity.timeSlot.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.Optional;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    Optional<TimeSlot> findByRestaurantIdAndStartTime(Long restaurantId, LocalTime startTime);

}
