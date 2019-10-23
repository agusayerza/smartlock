package com.smartlock.server.lock.persistence.model;

import com.smartlock.server.lock.presentation.dto.CreateLockDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Lock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String uid;
    private long userAdminId;
    private boolean active;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public Lock(CreateLockDto createLockDto, Long userAdminId) {
        this.uid = createLockDto.getUid();
        this.userAdminId = userAdminId;
        this.active = true;
        this.name = createLockDto.getName();
    }
}
