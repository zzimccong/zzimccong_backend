package com.project.zzimccong.repository.reservation;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.project.zzimccong.model.entity.reservation.QReservation.reservation;

@Repository
@RequiredArgsConstructor
public class ReservationDSLRepository {

    private final JPAQueryFactory queryFactory;

    // id와 role을 기반으로 예약 내역의 개수 조회
    public long countReservationsByIdAndRole(Integer id, String role) {
        Long count = queryFactory
                .select(reservation.count())
                .from(reservation)
                .where(role.equals("USER")
                        ? reservation.user.id.eq(id)
                        : reservation.corporation.id.eq(id))
                .fetchOne();

        // NullPointerException을 방지하기 위해 null일 경우 기본값 0을 반환
        return count != null ? count : 0L;
    }

    // id와 role을 기반으로 '노쇼' 상태인 예약 내역의 개수 조회
    public long countNoShowReservationsByIdAndRole(Integer id, String role) {
        Long count = queryFactory
                .select(reservation.count())
                .from(reservation)
                .where(role.equals("CORP")
                        ? reservation.corporation.id.eq(id).and(reservation.state.eq("노쇼"))
                        : reservation.user.id.eq(id).and(reservation.state.eq("노쇼")))
                .fetchOne();

        // NullPointerException을 방지하기 위해 null일 경우 기본값 0을 반환
        return count != null ? count : 0L;
    }
}
