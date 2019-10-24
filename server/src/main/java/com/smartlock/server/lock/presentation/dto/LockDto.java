package com.smartlock.server.lock.presentation.dto;

import com.smartlock.server.lock.persistence.model.Lock;

public class LockDto {
    private Long id;
    private String uid;
    private Long userAdminId;
    private String name;

    public LockDto() {
    }

    public LockDto(Lock lock) {
        this.id = lock.getId();
        this.uid = lock.getUuid();
        this.userAdminId = lock.getUserAdminId();
        this.name = lock.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getUserAdminId() {
        return userAdminId;
    }

    public void setUserAdminId(Long userAdminId) {
        this.userAdminId = userAdminId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
