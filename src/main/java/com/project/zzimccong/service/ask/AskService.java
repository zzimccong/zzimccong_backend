package com.project.zzimccong.service.ask;

import com.project.zzimccong.model.entity.ask.Ask;
import com.project.zzimccong.model.dto.ask.AskDTO;

import java.util.List;

public interface AskService {
    Ask createAsk(AskDTO AskDTO);
    List<Ask> getAsksByUser(Integer id, String role);
    Ask getAskById(Integer id);
    List<Ask> getAllAsks();
}
