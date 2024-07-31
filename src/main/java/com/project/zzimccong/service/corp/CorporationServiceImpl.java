package com.project.zzimccong.service.corp;

import com.project.zzimccong.model.dto.corp.CorporationDTO;
import com.project.zzimccong.model.entity.corp.Corporation;
import com.project.zzimccong.repository.corp.CorporationRepository;
import com.project.zzimccong.security.jwt.JwtTokenUtil;
import com.project.zzimccong.service.email.EmailVerificationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CorporationServiceImpl implements CorporationService {

    private final CorporationRepository corporationRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;

    public CorporationServiceImpl(CorporationRepository corporationRepository, JwtTokenUtil jwtTokenUtil, PasswordEncoder passwordEncoder, EmailVerificationService emailVerificationService) {
        this.corporationRepository = corporationRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
        this.emailVerificationService = emailVerificationService;
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

    @Override
    public String authenticate(String corpId, String password) {
        Optional<Corporation> corporationOptional = corporationRepository.findByCorpId(corpId);
        if (corporationOptional.isPresent()) {
            Corporation corporation = corporationOptional.get();
            if (passwordEncoder.matches(password, corporation.getPassword())) {
                return jwtTokenUtil.generateToken(corpId, "corp");
            }
        }
        return null;
    }

    @Override
    public Corporation getCorporationById(String corpId) {
        return corporationRepository.findByCorpId(corpId).orElseThrow(() -> new IllegalArgumentException("Corporation not found"));
    }

    @Override
    public Corporation updateCorporation(CorporationDTO corporationDTO) {
        Corporation corporation = corporationRepository.findByCorpId(corporationDTO.getCorpId())
                .orElseThrow(() -> new IllegalArgumentException("Corporation not found"));

        if (corporationDTO.getCorpName() != null) {
            corporation.setCorpName(corporationDTO.getCorpName());
        }
        if (corporationDTO.getCorpDept() != null) {
            corporation.setCorpDept(corporationDTO.getCorpDept());
        }
        if (corporationDTO.getCorpEmail() != null) {
            corporation.setCorpEmail(corporationDTO.getCorpEmail());
        }
        if (corporationDTO.getCorpAddress() != null) {
            corporation.setCorpAddress(corporationDTO.getCorpAddress());
        }
        if (corporationDTO.getPassword() != null && !corporationDTO.getPassword().isEmpty()) {
            corporation.setPassword(passwordEncoder.encode(corporationDTO.getPassword()));
        }

        return corporationRepository.save(corporation);
    }

    @Override
    public void changePassword(String corpId, String oldPassword, String newPassword) {
        Corporation corporation = corporationRepository.findByCorpId(corpId)
                .orElseThrow(() -> new IllegalArgumentException("Corporation not found"));

        if (!passwordEncoder.matches(oldPassword, corporation.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        corporation.setPassword(passwordEncoder.encode(newPassword));
        corporationRepository.save(corporation);
    }

    public boolean deleteCorporation(String corpId, String password) {
        Optional<Corporation> corporationOptional = corporationRepository.findByCorpId(corpId);
        if (corporationOptional.isPresent()) {
            Corporation corporation = corporationOptional.get();
            if (passwordEncoder.matches(password, corporation.getPassword())) {
                corporationRepository.delete(corporation);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Corporation> getAllCorporations() {
        return corporationRepository.findAll();
    }

    @Override
    public Corporation getCorporationByNameAndEmail(String name, String email) {
        return corporationRepository.findByCorpNameAndCorpEmail(name, email).orElse(null);
    }

    @Override
    public void sendTemporaryPassword(String corpId, String email) {
        emailVerificationService.sendTemporaryPassword(corpId, null, email);
    }

}
