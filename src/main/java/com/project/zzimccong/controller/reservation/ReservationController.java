package com.project.zzimccong.controller.reservation;

import com.project.zzimccong.model.dto.reservation.ReservationDTO;
import com.project.zzimccong.model.entity.reservation.Reservation;
import com.project.zzimccong.service.reservation.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public Reservation createReservation(@RequestBody Reservation reservation) {
        return reservationService.saveReservation(reservation);
    }

    @PutMapping("/{id}/status")
    public Reservation updateReservationStatus(@PathVariable Long id, @RequestBody String status) {
        return reservationService.updateReservationStatus(id, status);
    }


    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/user/{userId}")
    public List<ReservationDTO> getUserReservations(@PathVariable Integer userId) {
        return reservationService.getReservationsByUserId(userId);
    }

}
