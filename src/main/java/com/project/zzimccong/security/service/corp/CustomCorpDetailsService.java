package com.project.zzimccong.security.service.corp;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.project.zzimccong.model.entity.corp.Corporation;
import com.project.zzimccong.repository.corp.CorporationRepository;

@Service
public class CustomCorpDetailsService implements UserDetailsService {

    private final CorporationRepository corporationRepository;

    public CustomCorpDetailsService(CorporationRepository corporationRepository) {
        this.corporationRepository = corporationRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String corpId) throws UsernameNotFoundException {
        Corporation corporation = corporationRepository.findByCorpId(corpId)
                .orElseThrow(() -> new UsernameNotFoundException("Corporation not found with corpId: " + corpId));
        return new CorpDetails(corporation);
    }
}
