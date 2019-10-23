package com.smartlock.server.lock.presentation.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CreateLockDto {

    @NotNull
    @Size(min=6, max=6)
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


}
