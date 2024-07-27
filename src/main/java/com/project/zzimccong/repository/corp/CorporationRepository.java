package com.project.zzimccong.repository.corp;


import com.project.zzimccong.model.entity.corp.Corporation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CorporationRepository extends JpaRepository<Corporation, Integer> {
    Optional<Corporation> findByCorpEmail(String corpEmail);
    Optional<Corporation> findByCorpId(String corpId);
    boolean existsByCorpId(String corpId);
    Optional<Corporation> findByCorpIdAndPassword(String corpId, String password);
}
