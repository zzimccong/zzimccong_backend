package com.project.zzimccong.model.entity.ask;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAsk is a Querydsl query type for Ask
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAsk extends EntityPathBase<Ask> {

    private static final long serialVersionUID = 1345496522L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAsk ask = new QAsk("ask");

    public final StringPath askPassword = createString("askPassword");

    public final StringPath content = createString("content");

    public final com.project.zzimccong.model.entity.corp.QCorporation corporation;

    public final NumberPath<Integer> Id = createNumber("Id", Integer.class);

    public final BooleanPath secret = createBoolean("secret");

    public final StringPath title = createString("title");

    public final com.project.zzimccong.model.entity.user.QUser user;

    public QAsk(String variable) {
        this(Ask.class, forVariable(variable), INITS);
    }

    public QAsk(Path<? extends Ask> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAsk(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAsk(PathMetadata metadata, PathInits inits) {
        this(Ask.class, metadata, inits);
    }

    public QAsk(Class<? extends Ask> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.corporation = inits.isInitialized("corporation") ? new com.project.zzimccong.model.entity.corp.QCorporation(forProperty("corporation")) : null;
        this.user = inits.isInitialized("user") ? new com.project.zzimccong.model.entity.user.QUser(forProperty("user")) : null;
    }

}

