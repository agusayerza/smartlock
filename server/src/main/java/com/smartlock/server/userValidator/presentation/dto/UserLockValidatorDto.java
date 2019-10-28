package com.smartlock.server.userValidator.presentation.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserLockValidatorDto {

    @NotNull
    @Size(min = 5, max = 5)
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
