package com.project.zzimccong.service.reservation;

import com.project.zzimccong.model.dto.reservation.ReservationDTO;
import com.project.zzimccong.model.entity.reservation.Reservation;
import com.project.zzimccong.model.entity.user.User;

import java.util.List;

public interface ReservationService {
    Reservation saveReservation(Reservation reservation);
    List<Reservation> getAllReservations();
    Reservation updateReservationStatus(Long id, String status);
    List<ReservationDTO> getReservationsByUserId(Integer userId);


}
