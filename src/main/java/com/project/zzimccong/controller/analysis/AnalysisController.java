package com.project.zzimccong.controller.analysis;

import com.project.zzimccong.service.reservation.ReservationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    private final ReservationService reservationService;

    public AnalysisController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservation/count")
    public long getReservationCount(@RequestParam Integer id, @RequestParam String role) {
        return reservationService.getReservationCountByIdAndRole(id, role);
    }

    @GetMapping("/noshow/count")
    public long getNoShowReservationCount(@RequestParam Integer id, @RequestParam String role) {
        return reservationService.getNoShowReservationCountByIdAndRole(id, role);
    }

    @GetMapping("/noshow/rate")
    public int getNoShowRate(@RequestParam Integer id, @RequestParam String role) {
        return reservationService.calculateNoShowRate(id, role);
    }
}
