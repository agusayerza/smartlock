package com.smartlock.server.lock.presentation.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class UserLockDto {
    @NotNull
    private Long lockId;

    @NotNull
    @Email
    private String email;

    public UserLockDto(String email, Long lockId){
        this.email = email;
        this.lockId = lockId;
    }

    public UserLockDto() {
    }

    public Long getLockId() {
        return lockId;
    }

    public void setLockId(Long lockId) {
        this.lockId = lockId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
