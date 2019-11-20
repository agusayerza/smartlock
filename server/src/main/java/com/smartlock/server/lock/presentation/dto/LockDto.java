package com.smartlock.server.lock.presentation.dto;

import com.smartlock.server.lock.persistence.model.Lock;

public class LockDto {
    private Long id;
    private String uuid;
    private String name;
    private boolean opened;

    public LockDto() {
    }

    public LockDto(Lock lock) {
        this.id = lock.getId();
        this.uuid = lock.getUuid();
        this.name = lock.getName();
        this.opened = lock.isOpen();
    }

    public Long getId() {
        return id;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isOpened() {
        return opened;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
