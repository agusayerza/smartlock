package com.smartlock.server.security.presentation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtDto {
    private String token;
    private String type = "Bearer";
    private String email;

    @JsonIgnore
    private Collection<? extends GrantedAuthority> authorities;

    public JwtDto(String accessToken, String email, Collection<? extends GrantedAuthority> authorities) {
        this.token = accessToken;
        this.email = email;
        this.authorities = authorities;
    }

    public JwtDto() {
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
