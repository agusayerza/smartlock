package com.smartlock.server.security.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smartlock.server.user.persistence.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserPrinciple implements UserDetails {
	private static final long serialVersionUID = 1L;

    private final Long id;
    @JsonIgnore // We would never want to send the password to the front-end
    private final String password;
    private final String email;


    private final Collection<? extends GrantedAuthority> authorities;

    private UserPrinciple(Long id, String password, String email, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
    }

    static UserPrinciple build(User user) {
        Collection<GrantedAuthority> authorities = user.getRoles()
                .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return new UserPrinciple(
                user.getId(),
                user.getPassword(),
                user.getEmail(),
                authorities
        );
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        UserPrinciple user = (UserPrinciple) o;
        return Objects.equals(id, user.id);
    }

    public static UserPrinciple getUserPrinciple(){
        return (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}