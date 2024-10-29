package com.project.zzimccong.service.ask;

import com.project.zzimccong.model.dto.ask.AnswerDTO;
import com.project.zzimccong.model.entity.ask.Answer;
import com.project.zzimccong.model.entity.ask.Ask;
import com.project.zzimccong.repository.ask.AnswerRepository;
import com.project.zzimccong.repository.ask.AskDSLRepository;
import org.springframework.stereotype.Service;


@Service
public class AnswerServiceImpl implements AnswerService{

    private final AnswerRepository answerRepository;
    private final AskDSLRepository askDSLRepository;

    public AnswerServiceImpl(AnswerRepository answerRepository, AskDSLRepository askDSLRepository) {
        this.answerRepository = answerRepository;
        this.askDSLRepository = askDSLRepository;
    }


    @Override
    public void insertAnswer(AnswerDTO answerDTO) {
        Ask ask = askDSLRepository.findByAsk_id(answerDTO.getAsk_id());

        AnswerDTO answerDTOwithUser = new AnswerDTO();
        answerDTOwithUser.setAsk_id(answerDTO.getAsk_id());
        answerDTOwithUser.setContent(answerDTO.getContent());
        if(ask.getCorporation() == null && ask.getUser() != null) { //일반 유저
            answerDTOwithUser.setRole("USER"); //롤 설정
            answerDTOwithUser.setUserId(ask.getUser().getId()); //id 설정
            answerDTOwithUser.setUserName(ask.getUser().getName()); //name 설정
        }
        else { //기업 유저
            answerDTOwithUser.setRole("CORP");
            answerDTOwithUser.setCorpId(ask.getCorporation().getId()); //id 설정
            answerDTOwithUser.setCorpName(ask.getCorporation().getCorpName()); //name 설정
        }

        Answer answer = Answer.toEntity(answerDTOwithUser);
        answerRepository.save(answer);
    }

    @Override
    public AnswerDTO getAnswerByAskId(Integer askId) {
        Answer answer = answerRepository.findByAskId(askId);
        if(answer == null){
            return new AnswerDTO();
        }
        return AnswerDTO.toAnswerDTO(answer);
    }


}
