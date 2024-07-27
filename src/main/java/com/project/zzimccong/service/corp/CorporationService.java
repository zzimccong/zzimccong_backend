package com.project.zzimccong.service.corp;

import com.project.zzimccong.model.dto.corp.CorporationDTO;
import com.project.zzimccong.model.entity.corp.Corporation;

public interface CorporationService {
    Corporation registerCorporation(CorporationDTO corporationDTO);
    boolean isCorpEmailExists(String corpEmail);
    boolean isCorpIdExists(String corpId);
}
