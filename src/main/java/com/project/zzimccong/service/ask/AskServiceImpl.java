package com.project.zzimccong.service.ask;

import com.project.zzimccong.model.dto.ask.AskDTO;
import com.project.zzimccong.model.entity.ask.Ask;
import com.project.zzimccong.model.entity.corp.Corporation;
import com.project.zzimccong.model.entity.user.User;
import com.project.zzimccong.repository.ask.AskRepository;
import com.project.zzimccong.repository.corp.CorporationRepository;
import com.project.zzimccong.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AskServiceImpl implements AskService{

    private final AskRepository askRepository;
    private final CorporationRepository corporationRepository;
    private final UserRepository userRepository;

    public AskServiceImpl(AskRepository askRepository, CorporationRepository corporationRepository, UserRepository userRepository) {
        this.askRepository = askRepository;
        this.corporationRepository = corporationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Ask createAsk(AskDTO askDTO) {
        Ask ask = new Ask();

        if(askDTO.getRole().equals("USER")){
            Optional<User> userOptional=userRepository.findById(askDTO.getUserId());
            User user = userOptional.get();
            ask.setUser(user);
            ask.setCorporation(null);

        } else{
            Optional<Corporation> corporationOptional=corporationRepository.findById(askDTO.getCorpId());
            Corporation corporation = corporationOptional.get();
            ask.setCorporation(corporation);
            ask.setUser(null);
        }

        ask.setTitle(askDTO.getTitle());
        ask.setContent(askDTO.getContent());
        ask.setSecret(askDTO.getSecret());
        ask.setAskPassword(askDTO.getAskPassword());

        return askRepository.save(ask);
    }

    @Override
    public List<Ask> getAsksByUser(Integer id, String role) {
        if ("USER".equals(role)) {
            return askRepository.findByUser_Id(id);
        } else if ("CORP".equals(role)) {
            return askRepository.findByCorporation_Id(id);
        } else {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    @Override
    public Ask getAskById(Integer id) {
        return askRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 문의가 존재하지 않습니다."));
    }

    @Override
    public List<Ask> getAllAsks() {
        return askRepository.findAll();
    }
}
