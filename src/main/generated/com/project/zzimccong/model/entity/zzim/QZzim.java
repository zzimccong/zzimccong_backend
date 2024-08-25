package com.project.zzimccong.model.entity.zzim;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QZzim is a Querydsl query type for Zzim
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QZzim extends EntityPathBase<Zzim> {

    private static final long serialVersionUID = 1620405940L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QZzim zzim = new QZzim("zzim");

    public final com.project.zzimccong.model.entity.corp.QCorporation corporation;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final com.project.zzimccong.model.entity.store.QRestaurant restaurant;

    public final com.project.zzimccong.model.entity.user.QUser user;

    public QZzim(String variable) {
        this(Zzim.class, forVariable(variable), INITS);
    }

    public QZzim(Path<? extends Zzim> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QZzim(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QZzim(PathMetadata metadata, PathInits inits) {
        this(Zzim.class, metadata, inits);
    }

    public QZzim(Class<? extends Zzim> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.corporation = inits.isInitialized("corporation") ? new com.project.zzimccong.model.entity.corp.QCorporation(forProperty("corporation")) : null;
        this.restaurant = inits.isInitialized("restaurant") ? new com.project.zzimccong.model.entity.store.QRestaurant(forProperty("restaurant"), inits.get("restaurant")) : null;
        this.user = inits.isInitialized("user") ? new com.project.zzimccong.model.entity.user.QUser(forProperty("user")) : null;
    }

}

