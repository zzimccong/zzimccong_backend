package com.project.zzimccong.service.corp;

import com.project.zzimccong.model.dto.corp.CorporationDTO;
import com.project.zzimccong.model.entity.corp.Corporation;

import java.util.List;

public interface CorporationService {
    Corporation registerCorporation(CorporationDTO corporationDTO);

    boolean isCorpEmailExists(String corpEmail);

    boolean isCorpIdExists(String corpId);

    String authenticate(String corpId, String password);

    List<Corporation> getAllCorporations();

    Corporation getCorporationById(String corpId);
    Corporation updateCorporation(CorporationDTO corporationDTO);

    void changePassword(String corpId, String oldPassword, String newPassword);

    boolean deleteCorporation(String corpId, String password);

    Corporation getCorporationByNameAndEmail(String name, String email);

    void sendTemporaryPassword(String corpId, String email);
}
