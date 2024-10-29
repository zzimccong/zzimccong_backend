package com.project.zzimccong.service.reservation;

import com.project.zzimccong.model.dto.reservation.ReservationDTO;
import com.project.zzimccong.model.entity.corp.Corporation;
import com.project.zzimccong.model.entity.notification.NotificationToken;
import com.project.zzimccong.model.entity.reservation.Reservation;
import com.project.zzimccong.model.entity.store.Restaurant;
import com.project.zzimccong.model.entity.user.User;
import com.project.zzimccong.repository.corp.CorporationRepository;
import com.project.zzimccong.repository.coupon.CouponDSLRepository;
import com.project.zzimccong.repository.reservation.ReservationDSLRepository;
import com.project.zzimccong.repository.reservation.ReservationRepository;
import com.project.zzimccong.repository.store.RestaurantRepository;
import com.project.zzimccong.repository.user.UserRepository;
import com.project.zzimccong.security.jwt.JwtTokenUtil;
import com.project.zzimccong.service.coupon.CouponService;
import com.project.zzimccong.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private RestaurantRepository restaurantRepository;

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

    @Autowired
    private NotificationService notificationService;

    @Override
    @Transactional
    public Reservation saveReservation(ReservationDTO reservationDto, String token) {
        // JWT 토큰에서 userId 및 userType 추출
        String userId = jwtTokenUtil.getUserIdFromToken(token);
        String userType = jwtTokenUtil.getUserTypeFromToken(token);
        Restaurant restaurant = restaurantRepository.findById(reservationDto.getRestaurantId()).orElseThrow(() -> new RuntimeException("Restaurant not found"));
        Reservation reservation = reservationDto.toEntity();
        reservation.setRestaurant(restaurant);
        Reservation savedReservation;

        // 유저 타입에 따라 Reservation 엔터티의 필드를 설정
        if ("user".equalsIgnoreCase(userType) || "manager".equalsIgnoreCase(userType)) {
            // User 조회 시 Optional 처리
            Optional<User> userOptional = userRepository.findByLoginId(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                reservation.setUser(user); // userId 설정
                reservation.setCorporation(null); // corpId는 null로 설정
                // 예약 시 쿠폰 차감
                couponService.decreaseCouponCnt(user.getId());
                savedReservation = reservationRepository.save(reservation);
            } else {
                throw new RuntimeException("User not found with loginId: " + userId);
            }
        } else if ("corp".equalsIgnoreCase(userType)) {
            // Corporation 조회 시 Optional 처리
            Optional<Corporation> corpOptional = corporationRepository.findByCorpId(userId);
            if (corpOptional.isPresent()) {
                reservation.setCorporation(corpOptional.get()); // corpId 설정
                reservation.setUser(null); // userId는 null로 설정
                savedReservation = reservationRepository.save(reservation);
            } else {
                throw new RuntimeException("Corporation not found with corpId: " + userId);
            }
        } else {
            throw new IllegalArgumentException("Invalid user type");
        }

        // 예약이 성공적으로 저장된 후, manager에게 알림 전송
        sendNotificationToStoreManager(savedReservation);

        return savedReservation;
    }

    private void sendNotificationToStoreManager(Reservation reservation) {
        // 예약된 레스토랑에 연결된 매니저 찾기
        List<User> managers = restaurantRepository.findManagersByRestaurantId(reservation.getRestaurant().getId());

        String title = "새 예약 알림";
        String message = "새로운 예약이 생성되었습니다. 예약 ID: " + reservation.getId();

        if (!managers.isEmpty()) {
            for (User manager : managers) {
                // 매니저에 연결된 NotificationToken 조회
                String token = notificationService.getUserToken(manager.getId());
                if (token != null && !token.isEmpty()) {
                    try {
                        notificationService.sendMessage(token, title, message);
                    } catch (Exception e) {
                        log.error("관리자 {}에게 알림 전송 중 오류 발생: {}", manager.getLoginId(), e.getMessage());
                    }
                } else {
                    log.warn("관리자 {}의 알림 토큰을 찾을 수 없습니다.", manager.getLoginId());
                }
            }
        } else {
            log.warn("예약된 가게의 매니저를 찾을 수 없습니다.");
        }
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

            // 예약 상태가 "예약 확정" 또는 "예약 취소" 중 하나일 경우
            if ("예약 확정".equals(status) || "예약 취소".equals(status)) {
                // 예약한 유저에게 알림 발송
                sendNotificationToUser(reservation, status);
            }

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

    private void sendNotificationToUser(Reservation reservation, String status) {
        User user = reservation.getUser();
        if (user != null) {
            String title = "예약 상태 변경 알림";
            String message = "";

            if ("예약 확정".equals(status)) {
                message = "예약이 확정되었습니다.";
            } else if ("예약 취소".equals(status)) {
                message = "예약이 취소되었습니다.";
            }

            // 유저의 알림 토큰 조회
            String token = notificationService.getUserToken(user.getId());
            if (token != null && !token.isEmpty()) {
                try {
                    notificationService.sendMessage(token, title, message);
                } catch (Exception e) {
                    log.error("유저 {}에게 알림 전송 중 오류 발생: {}", user.getLoginId(), e.getMessage());
                }
            } else {
                log.warn("유저 {}의 알림 토큰을 찾을 수 없습니다.", user.getLoginId());
            }
        }
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
