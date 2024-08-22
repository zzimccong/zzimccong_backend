package com.project.zzimccong.service.reservation;

import com.project.zzimccong.model.dto.reservation.ReservationDTO;
import com.project.zzimccong.model.entity.corp.Corporation;
import com.project.zzimccong.model.entity.reservation.Reservation;
import com.project.zzimccong.model.entity.user.User;
import com.project.zzimccong.repository.corp.CorporationRepository;
import com.project.zzimccong.repository.coupon.CouponDSLRepository;
import com.project.zzimccong.repository.reservation.ReservationRepository;
import com.project.zzimccong.repository.user.UserRepository;
import com.project.zzimccong.security.jwt.JwtTokenUtil;
import com.project.zzimccong.service.coupon.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final CorporationRepository corporationRepository;

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponDSLRepository couponDSLService;

    public ReservationServiceImpl(
            ReservationRepository reservationRepository,
            JwtTokenUtil jwtTokenUtil,
            UserRepository userRepository,
            CorporationRepository corporationRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
        this.corporationRepository = corporationRepository;
    }

    @Override
    public Reservation saveReservation(Reservation reservation, String token) {
        // JWT 토큰에서 userId 및 userType 추출
        String userId = jwtTokenUtil.getUserIdFromToken(token);
        String userType = jwtTokenUtil.getUserTypeFromToken(token);

        // 유저 타입에 따라 Reservation 엔터티의 필드를 설정
        if ("user".equals(userType)) {
            // User 조회 시 Optional 처리
            Optional<User> userOptional = userRepository.findByLoginId(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                reservation.setUser(user); // userId 설정
                reservation.setCorporation(null); // corpId는 null로 설정

                // 예약 시 쿠폰 차감
                couponService.decreaseCouponCnt(user.getId());
            } else {
                throw new RuntimeException("User not found with loginId: " + userId);
            }
        } else if ("corp".equals(userType)) {
            // Corporation 조회 시 Optional 처리
            Optional<Corporation> corpOptional = corporationRepository.findByCorpId(userId);
            if (corpOptional.isPresent()) {
                reservation.setCorporation(corpOptional.get()); // corpId 설정
                reservation.setUser(null); // userId는 null로 설정
            } else {
                throw new RuntimeException("Corporation not found with corpId: " + userId);
            }
        } else {
            throw new IllegalArgumentException("Invalid user type");
        }

        return reservationRepository.save(reservation);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation updateReservationStatus(Long id, String status) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            reservation.setState(status);

            // 상태가 "예약 확정", "예약 취소", "방문 완료" 중 하나일 경우
            if ("예약 취소".equals(status) || "방문 완료".equals(status)) {
                // 예약 테이블에서 userId가 null이 아닐 때만 쿠폰 증가 호출
                if (reservation.getUser() != null) {
                    Integer userId = reservation.getUser().getId();
                    couponDSLService.increaseCntByUserId(userId);
                }
            }

            return reservationRepository.save(reservation);
        }
        throw new RuntimeException("Reservation not found");
    }

    @Override
    public List<ReservationDTO> getReservationsByUserId(Integer userId, String userType) {
        List<Reservation> reservations;

        // 유저 타입에 따라 다른 방식으로 예약 조회
        if ("USER".equals(userType)) {
            reservations = reservationRepository.findByUserId(userId);
        } else if ("CORP".equals(userType)) {
            reservations = reservationRepository.findByCorporationId(userId);
        } else {
            throw new IllegalArgumentException("Invalid user type");
        }

        // convertToDTO에서 유저 타입에 따라 null 값 설정
        return reservations.stream()
                .map(reservation -> convertToDTO(reservation, userType))
                .collect(Collectors.toList());
    }

    private ReservationDTO convertToDTO(Reservation reservation, String userType) {
        ReservationDTO dto = new ReservationDTO(
                reservation.getId(),
                null,
                null,
                reservation.getRestaurant().getId(),
                reservation.getReservationTime(),
                reservation.getReservationRegistrationTime(),
                reservation.getCount(),
                reservation.getState(),
                reservation.getRequest()
        );

        if ("user".equals(userType)) {
            dto.setUserId(reservation.getUser().getLoginId());
        } else if ("corp".equals(userType)) {
            dto.setCorpId(reservation.getCorporation().getCorpId());
        }

        return dto;
    }

}
