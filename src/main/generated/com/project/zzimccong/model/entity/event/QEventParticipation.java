package com.project.zzimccong.model.entity.event;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEventParticipation is a Querydsl query type for EventParticipation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEventParticipation extends EntityPathBase<EventParticipation> {

    private static final long serialVersionUID = 1502140693L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEventParticipation eventParticipation = new QEventParticipation("eventParticipation");

    public final com.project.zzimccong.model.entity.coupon.QCoupon coupon;

    public final QEvent event;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> usedCouponCount = createNumber("usedCouponCount", Integer.class);

    public final com.project.zzimccong.model.entity.user.QUser user;

    public final BooleanPath winner = createBoolean("winner");

    public QEventParticipation(String variable) {
        this(EventParticipation.class, forVariable(variable), INITS);
    }

    public QEventParticipation(Path<? extends EventParticipation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEventParticipation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEventParticipation(PathMetadata metadata, PathInits inits) {
        this(EventParticipation.class, metadata, inits);
    }

    public QEventParticipation(Class<? extends EventParticipation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.coupon = inits.isInitialized("coupon") ? new com.project.zzimccong.model.entity.coupon.QCoupon(forProperty("coupon"), inits.get("coupon")) : null;
        this.event = inits.isInitialized("event") ? new QEvent(forProperty("event"), inits.get("event")) : null;
        this.user = inits.isInitialized("user") ? new com.project.zzimccong.model.entity.user.QUser(forProperty("user")) : null;
    }

}

