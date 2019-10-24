package com.smartlock.server.schedule.presentation.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class GetWeekScheduleDto {

    @NotNull
    private long lockId;

    @NotNull
    @Email
    private String email;

    public long getLockId() {
        return lockId;
    }

    public void setLockId(long lockId) {
        this.lockId = lockId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
