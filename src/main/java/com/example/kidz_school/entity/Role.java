package com.example.kidz_school.entity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import static com.example.kidz_school.entity.Authority.*;

public enum Role {
    USER(Set.of(RESOURCE_READ)),
    ADMIN(Set.of(RESOURCE_WRITE));
    private final Set<Authority> authorities;

    Role(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();

        authorities.forEach(
                authority ->
                        grantedAuthorities.add(new SimpleGrantedAuthority(authority.name().toLowerCase())));
        grantedAuthorities.add(new SimpleGrantedAuthority(this.name()));

        return grantedAuthorities;
    }
}
