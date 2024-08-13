package com.project.zzimccong.service.reservation;

import com.project.zzimccong.model.dto.reservation.ReservationDTO;
import com.project.zzimccong.model.entity.reservation.Reservation;
import com.project.zzimccong.repository.reservation.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public List<ReservationDTO> getReservationsByUserId(Integer userId) {
        List<Reservation> reservations = reservationRepository.findByUserId(userId);
        return reservations.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private ReservationDTO convertToDTO(Reservation reservation) {
        return new ReservationDTO(
                reservation.getId(),
                reservation.getUser().getId(),  // User ID 설정
                reservation.getRestaurant().getId(),  // Restaurant ID 설정
                reservation.getReservationTime(),
                reservation.getReservationRegistrationTime(),
                reservation.getCount(),
                reservation.getState(),
                reservation.getRequest()
        );
    }
}
