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
    @Column(length=1000000)
    @ElementCollection(targetClass=Long.class, fetch = FetchType.EAGER)
    private List<Long> locksId;

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

    public List<Long> getLocksId() {
        return locksId;
    }

    public void setLocksId(List<Long> locksId) {
        this.locksId = locksId;
    }

    public List<String> getRoles() {
        ArrayList<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        return roles;
    }

    public User(CreateUserDto createUserDto) {
        this.email = createUserDto.getEmail();
        this.password = createUserDto.getPassword();
        this.locksId = new ArrayList<>();
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
