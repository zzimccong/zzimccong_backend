package com.project.zzimccong.service.reservation;

import com.project.zzimccong.model.dto.reservation.ReservationDTO;
import com.project.zzimccong.model.entity.corp.Corporation;
import com.project.zzimccong.model.entity.reservation.Reservation;
import com.project.zzimccong.model.entity.user.User;
import com.project.zzimccong.repository.corp.CorporationRepository;
import com.project.zzimccong.repository.coupon.CouponDSLRepository;
import com.project.zzimccong.repository.reservation.ReservationDSLRepository;
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

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CorporationRepository corporationRepository;

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponDSLRepository couponDSLService;

    @Autowired
    private ReservationDSLRepository reservationDSLRepository;

    @Override
    public Reservation saveReservation(Reservation reservation, String token) {
        // JWT 토큰에서 userId 및 userType 추출
        String userId = jwtTokenUtil.getUserIdFromToken(token);
        String userType = jwtTokenUtil.getUserTypeFromToken(token);

        // 유저 타입에 따라 Reservation 엔터 티의 필드를 설정
        if ("user".equals(userType) || "manager".equals(userType)) {
            // User 조회 시 Optional 처리
            Optional<User> userOptional = userRepository.findByLoginId(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                reservation.setUser(user); // userId 설정
                reservation.setCorporation(null); // corpId는 null로 설정
                System.out.println(user);
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
    public List<ReservationDTO> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(this::convertDTO)
                .collect(Collectors.toList());
    }

    private ReservationDTO convertDTO(Reservation reservation) {
        String userId = reservation.getUser() != null ? String.valueOf(reservation.getUser().getId()) : null;
        String corpId = reservation.getCorporation() != null ? String.valueOf(reservation.getCorporation().getId()) : null;

        String userName = reservation.getUser() != null ? String.valueOf(reservation.getUser().getName()) : null;
        String corpName = reservation.getCorporation() != null ? String.valueOf(reservation.getCorporation().getCorpName()) : null;

        return new ReservationDTO(
                reservation.getId(),
                userId,
                corpId,
                userName,
                corpName,
                reservation.getRestaurant().getId(),
                reservation.getReservationTime(),
                reservation.getReservationRegistrationTime(),
                reservation.getCount(),
                reservation.getState(),
                reservation.getRequest()
        );
    }

    @Override
    public Reservation updateReservationStatus(Long id, String status) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            reservation.setState(status);

            // 상태가 "예약 확정", "예약 취소", "방문 완료" 중 하나일 경우
            if ("예약 확정".equals(status) || "예약 취소".equals(status) || "방문 완료".equals(status)) {
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
        } else if ("MANAGER".equals(userType)) {
            reservations = reservationRepository.findByUserId(userId); // 예시
        } else {
            throw new IllegalArgumentException("Invalid user type: " + userType);
        }

        return reservations.stream()
                .map(reservation -> convertToDTO(reservation, userType))
                .collect(Collectors.toList());
    }

    private ReservationDTO convertToDTO(Reservation reservation, String userType) {
        ReservationDTO dto = new ReservationDTO(
                reservation.getId(),
                null,
                null,
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
            dto.setUserName(reservation.getUser().getName());
        } else if ("corp".equals(userType)) {
            dto.setCorpId(reservation.getCorporation().getCorpId());
            dto.setCorpName(reservation.getCorporation().getCorpName());
        }

        return dto;
    }

    // id와 role을 기반으로 '노쇼' 상태인 예약 내역의 개수 조회
    @Override
    public long getNoShowReservationCountByIdAndRole(Integer id, String role) {
        return reservationDSLRepository.countNoShowReservationsByIdAndRole(id, role);
    }

    // id와 role을 기반으로 예약 내역의 개수 조회
    @Override
    public long getReservationCountByIdAndRole(Integer id, String role) {
        return reservationDSLRepository.countReservationsByIdAndRole(id, role);
    }

    // 노쇼 확률 계산 (정수 반환)
    @Override
    public int calculateNoShowRate(Integer id, String role) {
        long totalReservations = getReservationCountByIdAndRole(id, role);
        long noShowCount = getNoShowReservationCountByIdAndRole(id, role);

        // 총 예약 개수가 0인 경우, 노쇼 확률은 0으로 처리
        if (totalReservations == 0) {
            return 0;
        }

        // 노쇼 확률 계산: (노쇼 개수 / 총 예약 개수) * 100, 소수점 제외
        double noShowRate = ((double) noShowCount / totalReservations) * 100;
        return (int) noShowRate;
    }
}
