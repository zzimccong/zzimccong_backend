package com.project.zzimccong.model.entity.corp;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCorporation is a Querydsl query type for Corporation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCorporation extends EntityPathBase<Corporation> {

    private static final long serialVersionUID = 1802759678L;

    public static final QCorporation corporation = new QCorporation("corporation");

    public final StringPath corpAddress = createString("corpAddress");

    public final StringPath corpDept = createString("corpDept");

    public final StringPath corpEmail = createString("corpEmail");

    public final StringPath corpId = createString("corpId");

    public final StringPath corpName = createString("corpName");

    public final ListPath<com.project.zzimccong.model.entity.email.EmailVerification, com.project.zzimccong.model.entity.email.QEmailVerification> emailVerifications = this.<com.project.zzimccong.model.entity.email.EmailVerification, com.project.zzimccong.model.entity.email.QEmailVerification>createList("emailVerifications", com.project.zzimccong.model.entity.email.EmailVerification.class, com.project.zzimccong.model.entity.email.QEmailVerification.class, PathInits.DIRECT2);

    public final BooleanPath emailVerified = createBoolean("emailVerified");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath password = createString("password");

    public final StringPath role = createString("role");

    public QCorporation(String variable) {
        super(Corporation.class, forVariable(variable));
    }

    public QCorporation(Path<? extends Corporation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCorporation(PathMetadata metadata) {
        super(Corporation.class, metadata);
    }

}

