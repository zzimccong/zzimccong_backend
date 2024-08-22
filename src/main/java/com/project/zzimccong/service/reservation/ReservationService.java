package com.project.zzimccong.service.reservation;

import com.project.zzimccong.model.dto.reservation.ReservationDTO;
import com.project.zzimccong.model.entity.reservation.Reservation;
import com.project.zzimccong.model.entity.user.User;

import java.util.List;

public interface ReservationService {
    Reservation saveReservation(Reservation reservation, String token);
    List<ReservationDTO> getAllReservations();
    Reservation updateReservationStatus(Long id, String status);
    List<ReservationDTO> getReservationsByUserId(Integer userId, String userType);


    long getNoShowReservationCountByIdAndRole(Integer id, String role);
    long getReservationCountByIdAndRole(Integer id, String role);
    int calculateNoShowRate(Integer id, String role);
}
