package com.project.zzimccong.model.entity.sms;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSMSVerification is a Querydsl query type for SMSVerification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSMSVerification extends EntityPathBase<SMSVerification> {

    private static final long serialVersionUID = -1154555195L;

    public static final QSMSVerification sMSVerification = new QSMSVerification("sMSVerification");

    public final StringPath code = createString("code");

    public final DateTimePath<java.time.LocalDateTime> expirationTime = createDateTime("expirationTime", java.time.LocalDateTime.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath phone = createString("phone");

    public QSMSVerification(String variable) {
        super(SMSVerification.class, forVariable(variable));
    }

    public QSMSVerification(Path<? extends SMSVerification> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSMSVerification(PathMetadata metadata) {
        super(SMSVerification.class, metadata);
    }

}

