package com.project.zzimccong.controller.reservation;

import com.project.zzimccong.model.dto.reservation.ReservationDTO;
import com.project.zzimccong.model.entity.reservation.Reservation;
import com.project.zzimccong.model.entity.user.User;
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

    @PostMapping
    public Reservation createReservation(@RequestBody Reservation reservation) {
        System.out.println("Received restaurantId: " + reservation.getRestaurant());

        return reservationService.saveReservation(reservation);
    }

    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @PutMapping("/{id}/status")
    public Reservation updateReservationStatus(@PathVariable Long id, @RequestBody String status) {
        return reservationService.updateReservationStatus(id, status);
    }


    @GetMapping("/user/{userId}")
    public List<ReservationDTO> getUserReservations(@PathVariable Integer userId) {
        return reservationService.getReservationsByUserId(userId);
    }

    @GetMapping("/{userId}/visited")
    public List<Reservation> getVisitedReservation(@PathVariable Integer userId) {
        User user = userService.FindById(userId);
        return reservationService.findByUserAndState(user, "방문완료");
    }

}
