package com.project.zzimccong.model.entity.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -654051564L;

    public static final QUser user = new QUser("user");

    public final DatePath<java.time.LocalDate> birth = createDate("birth", java.time.LocalDate.class);

    public final StringPath email = createString("email");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath loginId = createString("loginId");

    public final StringPath name = createString("name");

    public final ListPath<com.project.zzimccong.model.entity.event.EventParticipation, com.project.zzimccong.model.entity.event.QEventParticipation> participations = this.<com.project.zzimccong.model.entity.event.EventParticipation, com.project.zzimccong.model.entity.event.QEventParticipation>createList("participations", com.project.zzimccong.model.entity.event.EventParticipation.class, com.project.zzimccong.model.entity.event.QEventParticipation.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final StringPath phone = createString("phone");

    public final ListPath<com.project.zzimccong.model.entity.reservation.Reservation, com.project.zzimccong.model.entity.reservation.QReservation> reservations = this.<com.project.zzimccong.model.entity.reservation.Reservation, com.project.zzimccong.model.entity.reservation.QReservation>createList("reservations", com.project.zzimccong.model.entity.reservation.Reservation.class, com.project.zzimccong.model.entity.reservation.QReservation.class, PathInits.DIRECT2);

    public final ListPath<com.project.zzimccong.model.entity.store.Restaurant, com.project.zzimccong.model.entity.store.QRestaurant> restaurants = this.<com.project.zzimccong.model.entity.store.Restaurant, com.project.zzimccong.model.entity.store.QRestaurant>createList("restaurants", com.project.zzimccong.model.entity.store.Restaurant.class, com.project.zzimccong.model.entity.store.QRestaurant.class, PathInits.DIRECT2);

    public final StringPath role = createString("role");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

