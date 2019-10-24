package com.smartlock.server.user.presentation.dto;

import com.smartlock.server.user.persistence.model.User;

public class UserWithoutLocksDto {
    private Long id;
    private String email;

    public UserWithoutLocksDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail().toLowerCase();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
