package com.project.zzimccong.service.ask;

import com.project.zzimccong.model.dto.ask.AnswerDTO;

public interface AnswerService {
    void insertAnswer(AnswerDTO answerDTO);
    AnswerDTO getAnswerByAskId(Integer askId);
}
