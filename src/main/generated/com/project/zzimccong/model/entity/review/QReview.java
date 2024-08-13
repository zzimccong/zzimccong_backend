package com.project.zzimccong.model.entity.review;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = -1840849228L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReview review = new QReview("review");

    public final StringPath content = createString("content");

    public final NumberPath<Integer> convenient = createNumber("convenient", Integer.class);

    public final NumberPath<Integer> corpId = createNumber("corpId", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> mood = createNumber("mood", Integer.class);

    public final NumberPath<Double> rate = createNumber("rate", Double.class);

    public final com.project.zzimccong.model.entity.reservation.QReservation reservation;

    public final NumberPath<Integer> taste = createNumber("taste", Integer.class);

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public QReview(String variable) {
        this(Review.class, forVariable(variable), INITS);
    }

    public QReview(Path<? extends Review> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReview(PathMetadata metadata, PathInits inits) {
        this(Review.class, metadata, inits);
    }

    public QReview(Class<? extends Review> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reservation = inits.isInitialized("reservation") ? new com.project.zzimccong.model.entity.reservation.QReservation(forProperty("reservation"), inits.get("reservation")) : null;
    }

}

