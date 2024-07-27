package com.project.zzimccong.security.service.corp;

import com.project.zzimccong.model.entity.corp.Corporation;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.Collections;

public class CorpDetails implements UserDetails {

    private final Corporation corporation;

    public CorpDetails(Corporation corporation) {
        this.corporation = corporation;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return corporation.getPassword();
    }

    @Override
    public String getUsername() {
        return corporation.getCorpId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return corporation.isEmailVerified();
    }

    public Corporation getCorporation() {
        return corporation;
    }
}
