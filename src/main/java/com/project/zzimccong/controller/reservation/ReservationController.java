package com.project.zzimccong.controller.reservation;

import com.project.zzimccong.model.dto.reservation.ReservationDTO;
import com.project.zzimccong.model.entity.reservation.Reservation;
import com.project.zzimccong.security.jwt.JwtTokenUtil;
import com.project.zzimccong.service.reservation.ReservationService;
import com.project.zzimccong.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Reservation createReservation(@RequestBody ReservationDTO reservation, @RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");

        log.trace("Received reservation request: {}", reservation);
        log.debug("Authorization token: {}", token);

        Reservation savedReservation = reservationService.saveReservation(reservation, token);

        log.trace("Saved reservation: {}", savedReservation);
        return savedReservation;
    }

    @GetMapping
    public List<ReservationDTO> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @PutMapping("/{id}/status")
    public Reservation updateReservationStatus(@PathVariable Long id, @RequestBody String status) {
        return reservationService.updateReservationStatus(id, status);
    }


    @GetMapping("/user")
    public List<ReservationDTO> getUserReservations(@RequestParam("userId") Integer userId, @RequestParam("userType") String userType) {
        return reservationService.getReservationsByUserId(userId, userType);
    }

}
