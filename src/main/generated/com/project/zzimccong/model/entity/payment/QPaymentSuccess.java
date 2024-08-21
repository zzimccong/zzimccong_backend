package com.project.zzimccong.model.entity.payment;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPaymentSuccess is a Querydsl query type for PaymentSuccess
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPaymentSuccess extends EntityPathBase<PaymentSuccess> {

    private static final long serialVersionUID = -1947739393L;

    public static final QPaymentSuccess paymentSuccess = new QPaymentSuccess("paymentSuccess");

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final NumberPath<Integer> customer = createNumber("customer", Integer.class);

    public final StringPath orderId = createString("orderId");

    public final StringPath orderName = createString("orderName");

    public final DateTimePath<java.time.LocalDateTime> paymentDate = createDateTime("paymentDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> paymentId = createNumber("paymentId", Long.class);

    public final EnumPath<com.project.zzimccong.model.dto.payment.PayType> payType = createEnum("payType", com.project.zzimccong.model.dto.payment.PayType.class);

    public QPaymentSuccess(String variable) {
        super(PaymentSuccess.class, forVariable(variable));
    }

    public QPaymentSuccess(Path<? extends PaymentSuccess> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPaymentSuccess(PathMetadata metadata) {
        super(PaymentSuccess.class, metadata);
    }

}

