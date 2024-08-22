package com.project.zzimccong.controller.reservation;

import com.project.zzimccong.model.dto.reservation.ReservationDTO;
import com.project.zzimccong.model.entity.reservation.Reservation;
import com.project.zzimccong.security.jwt.JwtTokenUtil;
import com.project.zzimccong.service.reservation.ReservationService;
import com.project.zzimccong.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping
    public Reservation createReservation(@RequestBody Reservation reservation, @RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        return reservationService.saveReservation(reservation, token);
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
