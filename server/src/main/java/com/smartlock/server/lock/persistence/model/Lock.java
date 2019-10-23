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
//    todo validar que esto sea unique. si existe y active false, pasarlo a true y setear nuevo admin
    private String uid;
    private long userAdminId;
    private boolean active;

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

    public Lock(CreateLockDto createLockDto, Long userAdminId) {
        this.uid = createLockDto.getUid();
        this.userAdminId = userAdminId;
        this.active = true;
    }
}
