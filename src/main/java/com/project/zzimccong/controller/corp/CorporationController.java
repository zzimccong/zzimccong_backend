package com.project.zzimccong.controller.corp;

import com.project.zzimccong.model.dto.corp.CorporationDTO;
import com.project.zzimccong.model.entity.corp.Corporation;
import com.project.zzimccong.service.corp.CorporationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/corporations")
public class CorporationController {

    private final CorporationService corporationService;

    public CorporationController(CorporationService corporationService) {
        this.corporationService = corporationService;
    }

    @PostMapping("/corp-register")
    public ResponseEntity<String> registerCorporation(@RequestBody CorporationDTO corporationDTO) {
        if (corporationService.isCorpEmailExists(corporationDTO.getCorpEmail())) {
            return ResponseEntity.badRequest().body("Email already in use");
        }
        if (corporationService.isCorpIdExists(corporationDTO.getCorpId())) {
            return ResponseEntity.badRequest().body("ID already in use");
        }
        try {
            Corporation corporation = corporationService.registerCorporation(corporationDTO);
            return ResponseEntity.ok("Registration successful. Verification email sent to: " + corporation.getCorpEmail());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Registration failed: " + e.getMessage());
        }
    }
}
