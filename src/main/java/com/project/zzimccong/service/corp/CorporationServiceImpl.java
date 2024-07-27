package com.project.zzimccong.service.corp;

import com.project.zzimccong.model.dto.corp.CorporationDTO;
import com.project.zzimccong.model.entity.corp.Corporation;
import com.project.zzimccong.repository.corp.CorporationRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CorporationServiceImpl implements CorporationService {

    private final CorporationRepository corporationRepository;
    private final PasswordEncoder passwordEncoder;

    public CorporationServiceImpl(CorporationRepository corporationRepository, PasswordEncoder passwordEncoder) {
        this.corporationRepository = corporationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Corporation registerCorporation(CorporationDTO corporationDTO) {
        if (!corporationDTO.isEmailVerified()) {
            throw new IllegalArgumentException("Email not verified");
        }

        Corporation corporation = new Corporation();
        corporation.setCorpName(corporationDTO.getCorpName());
        corporation.setCorpDept(corporationDTO.getCorpDept());
        corporation.setCorpId(corporationDTO.getCorpId());

        if (corporationDTO.getPassword() == null || corporationDTO.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        String encodedPassword = passwordEncoder.encode(corporationDTO.getPassword());
        corporation.setPassword(encodedPassword);

        corporation.setCorpEmail(corporationDTO.getCorpEmail());
        corporation.setCorpAddress(corporationDTO.getCorpAddress());
        corporation.setRole(corporationDTO.getRole());
        corporation.setEmailVerified(corporationDTO.isEmailVerified());

        return corporationRepository.save(corporation);
    }

    @Override
    public boolean isCorpEmailExists(String corpEmail) {
        return corporationRepository.findByCorpEmail(corpEmail).isPresent();
    }

    @Override
    public boolean isCorpIdExists(String corpId) {
        return corporationRepository.existsByCorpId(corpId);
    }
}
