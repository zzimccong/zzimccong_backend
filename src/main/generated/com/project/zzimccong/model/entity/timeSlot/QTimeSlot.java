package com.project.zzimccong.model.entity.timeSlot;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTimeSlot is a Querydsl query type for TimeSlot
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTimeSlot extends EntityPathBase<TimeSlot> {

    private static final long serialVersionUID = 1160526740L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTimeSlot timeSlot = new QTimeSlot("timeSlot");

    public final NumberPath<Integer> availableSeats = createNumber("availableSeats", Integer.class);

    public final TimePath<java.time.LocalTime> endTime = createTime("endTime", java.time.LocalTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.project.zzimccong.model.entity.store.QRestaurant restaurant;

    public final TimePath<java.time.LocalTime> startTime = createTime("startTime", java.time.LocalTime.class);

    public final NumberPath<Integer> totalSeats = createNumber("totalSeats", Integer.class);

    public QTimeSlot(String variable) {
        this(TimeSlot.class, forVariable(variable), INITS);
    }

    public QTimeSlot(Path<? extends TimeSlot> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTimeSlot(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTimeSlot(PathMetadata metadata, PathInits inits) {
        this(TimeSlot.class, metadata, inits);
    }

    public QTimeSlot(Class<? extends TimeSlot> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.restaurant = inits.isInitialized("restaurant") ? new com.project.zzimccong.model.entity.store.QRestaurant(forProperty("restaurant"), inits.get("restaurant")) : null;
    }

}

