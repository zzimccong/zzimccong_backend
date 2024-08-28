package com.project.zzimccong.repository.ask;

import com.project.zzimccong.model.entity.ask.Ask;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.project.zzimccong.model.entity.ask.QAsk.ask;

@Repository
@RequiredArgsConstructor
public class AskDSLRepository {

    private final JPAQueryFactory queryFactory;

    public Ask findByAsk_id(Integer ask_id){
        return queryFactory
                .selectDistinct(ask)
                .from(ask)
                .where(ask.Id.eq(ask_id))
                .fetchOne();
    }


}
