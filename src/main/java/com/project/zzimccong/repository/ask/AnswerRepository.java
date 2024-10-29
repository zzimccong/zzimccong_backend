package com.project.zzimccong.repository.ask;

import com.project.zzimccong.model.entity.ask.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    Answer findByAskId(int askId);
}
