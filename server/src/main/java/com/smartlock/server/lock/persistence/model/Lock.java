package com.smartlock.server.lock.persistence.model;

import com.smartlock.server.lock.presentation.dto.CreateLockDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="lockEntity")
public class Lock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String uuid;
    private Long userAdminId;
    private boolean active;
    private String name;
    private boolean isOpen;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getUserAdminId() {
        return userAdminId;
    }

    public void setUserAdminId(long userAdminId) {
        this.userAdminId = userAdminId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserAdminId(Long userAdminId) {
        this.userAdminId = userAdminId;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public Lock(CreateLockDto createLockDto, Long userAdminId) {
        this.uuid = createLockDto.getUid();
        this.userAdminId = userAdminId;
        this.active = true;
        this.name = createLockDto.getName();
        this.isOpen = true;
    }

    public Lock() {
        this.isOpen = true;
    }
}
