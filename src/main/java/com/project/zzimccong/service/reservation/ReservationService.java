package com.project.zzimccong.service.reservation;

import com.project.zzimccong.model.entity.reservation.Reservation;

import java.util.List;

public interface ReservationService {
    Reservation saveReservation(Reservation reservation);
    List<Reservation> getAllReservations();
}
