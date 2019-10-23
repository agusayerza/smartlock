package com.smartlock.server.user.presentation.dto;

import com.smartlock.server.user.persistence.model.User;

import java.util.List;

public class UserDto {
    private Long id;
    private List<Long> locksId;
    private String email;

    public UserDto() {
    }

    public UserDto(User user) {
        this.id = user.getId();
        this.locksId = user.getLocksId();
        this.email = user.getEmail().toLowerCase();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getLocksId() {
        return locksId;
    }

    public void setLocksId(List<Long> locksId) {
        this.locksId = locksId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}