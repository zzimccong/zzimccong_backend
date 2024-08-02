package com.project.zzimccong.model.entity.email;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEmailVerification is a Querydsl query type for EmailVerification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmailVerification extends EntityPathBase<EmailVerification> {

    private static final long serialVersionUID = 1462534731L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmailVerification emailVerification = new QEmailVerification("emailVerification");

    public final StringPath corpEmail = createString("corpEmail");

    public final com.project.zzimccong.model.entity.corp.QCorporation corporation;

    public final DateTimePath<java.time.LocalDateTime> expirationTime = createDateTime("expirationTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath verificationCode = createString("verificationCode");

    public final BooleanPath verified = createBoolean("verified");

    public QEmailVerification(String variable) {
        this(EmailVerification.class, forVariable(variable), INITS);
    }

    public QEmailVerification(Path<? extends EmailVerification> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEmailVerification(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEmailVerification(PathMetadata metadata, PathInits inits) {
        this(EmailVerification.class, metadata, inits);
    }

    public QEmailVerification(Class<? extends EmailVerification> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.corporation = inits.isInitialized("corporation") ? new com.project.zzimccong.model.entity.corp.QCorporation(forProperty("corporation")) : null;
    }

}

