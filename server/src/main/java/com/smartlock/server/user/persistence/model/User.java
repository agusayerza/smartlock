package com.smartlock.server.user.persistence.model;

import com.smartlock.server.user.presentation.dto.CreateUserDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String email;
    private String password;
//  todo esto cuan largo es?
    @Column(length=1000000)
    @ElementCollection(targetClass=Long.class, fetch = FetchType.EAGER)
    private List<Long> locksId;
    private boolean active;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    public String getPassword() {
        return this.password;
    }

    public String getUsername() {
        return this.email;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Long> getLocksId() {
        return locksId;
    }

    public void setLocksId(List<Long> locksId) {
        this.locksId = locksId;
    }

    public List<String> getRoles() {
        return roles;
    }

    public User(CreateUserDto createUserDto) {
        this.email = createUserDto.getEmail();
        this.password = createUserDto.getPassword();
        this.locksId = new ArrayList<>();
        this.roles = new ArrayList<>();
        this.roles.add("ROLE_USER");
        this.active = true;
    }

    public User() {
    }

    public boolean addNewLock(Long lockId) {
        return locksId.add(lockId);
    }

    public boolean removeLock(Long lockId) {
        return locksId.remove(lockId);
    }
}
