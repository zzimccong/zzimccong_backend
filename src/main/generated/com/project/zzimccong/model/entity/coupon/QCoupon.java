package com.project.zzimccong.model.entity.coupon;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCoupon is a Querydsl query type for Coupon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCoupon extends EntityPathBase<Coupon> {

    private static final long serialVersionUID = 157088500L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCoupon coupon = new QCoupon("coupon");

    public final NumberPath<Integer> cnt = createNumber("cnt", Integer.class);

    public final NumberPath<Integer> discountPrice = createNumber("discountPrice", Integer.class);

    public final com.project.zzimccong.model.entity.event.QEvent event;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath type = createString("type");

    public final BooleanPath used = createBoolean("used");

    public final com.project.zzimccong.model.entity.user.QUser user;

    public QCoupon(String variable) {
        this(Coupon.class, forVariable(variable), INITS);
    }

    public QCoupon(Path<? extends Coupon> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCoupon(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCoupon(PathMetadata metadata, PathInits inits) {
        this(Coupon.class, metadata, inits);
    }

    public QCoupon(Class<? extends Coupon> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.event = inits.isInitialized("event") ? new com.project.zzimccong.model.entity.event.QEvent(forProperty("event"), inits.get("event")) : null;
        this.user = inits.isInitialized("user") ? new com.project.zzimccong.model.entity.user.QUser(forProperty("user")) : null;
    }

}

