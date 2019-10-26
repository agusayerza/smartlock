package com.smartlock.server.userValidator.presentation.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserLockValidatorDto {
    @NotNull
    private Long lockId;

    @NotNull
    @Size(min = 5, max = 5)
    private String code;

    public Long getLockId() {
        return lockId;
    }

    public void setLockId(Long lockId) {
        this.lockId = lockId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
