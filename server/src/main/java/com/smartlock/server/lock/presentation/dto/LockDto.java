package com.smartlock.server.lock.presentation.dto;

import com.smartlock.server.lock.persistence.model.Lock;

public class LockDto {
    private Long id;
    private String uuid;
    public LockDto() {
    }

    public LockDto(Lock lock) {
        this.id = lock.getId();
        this.uuid = lock.getUuid();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


}
