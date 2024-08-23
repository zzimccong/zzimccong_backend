package com.project.zzimccong.model.entity.notification;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNotificationToken is a Querydsl query type for NotificationToken
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNotificationToken extends EntityPathBase<NotificationToken> {

    private static final long serialVersionUID = -1508668859L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNotificationToken notificationToken = new QNotificationToken("notificationToken");

    public final com.project.zzimccong.model.entity.corp.QCorporation corporation;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath token = createString("token");

    public final com.project.zzimccong.model.entity.user.QUser user;

    public QNotificationToken(String variable) {
        this(NotificationToken.class, forVariable(variable), INITS);
    }

    public QNotificationToken(Path<? extends NotificationToken> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNotificationToken(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNotificationToken(PathMetadata metadata, PathInits inits) {
        this(NotificationToken.class, metadata, inits);
    }

    public QNotificationToken(Class<? extends NotificationToken> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.corporation = inits.isInitialized("corporation") ? new com.project.zzimccong.model.entity.corp.QCorporation(forProperty("corporation")) : null;
        this.user = inits.isInitialized("user") ? new com.project.zzimccong.model.entity.user.QUser(forProperty("user")) : null;
    }

}

