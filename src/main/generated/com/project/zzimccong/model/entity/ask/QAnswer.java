package com.project.zzimccong.model.entity.ask;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAnswer is a Querydsl query type for Answer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAnswer extends EntityPathBase<Answer> {

    private static final long serialVersionUID = -1247148339L;

    public static final QAnswer answer = new QAnswer("answer");

    public final NumberPath<Integer> askId = createNumber("askId", Integer.class);

    public final StringPath content = createString("content");

    public final NumberPath<Integer> corp_id = createNumber("corp_id", Integer.class);

    public final StringPath corpName = createString("corpName");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> user_id = createNumber("user_id", Integer.class);

    public final StringPath userName = createString("userName");

    public QAnswer(String variable) {
        super(Answer.class, forVariable(variable));
    }

    public QAnswer(Path<? extends Answer> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAnswer(PathMetadata metadata) {
        super(Answer.class, metadata);
    }

}

