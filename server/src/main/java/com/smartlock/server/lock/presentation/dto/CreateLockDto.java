package com.smartlock.server.lock.presentation.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CreateLockDto {

    @NotNull
    @Size(min=36, max=36)
    @Pattern(regexp = "([a-f0-9]{8}(-[a-f0-9]{4}){3}-[a-f0-9]{12})")
    private String uid;
    @Size(min=4, max=32)
    private String name;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
