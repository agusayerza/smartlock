package com.smartlock.server.userValidator.persistence.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class UserValidator implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String email;
    private Long lockId;
    private String code;

    public UserValidator() {
    }

    public UserValidator(String email, Long lockId){
        this.email = email;
        this.code = generateCode();
        this.lockId = lockId;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getLockId() {
        return lockId;
    }

    public void setLockId(Long lockId) {
        this.lockId = lockId;
    }

    public String generateCode(){
        StringBuilder code = new StringBuilder();
        char randomLetter;
        int random;
        for (int i = 0; i < 5; i++) {
            random = (int) Math.floor(Math.random() * 26 + 65);
            randomLetter = (char) random;
            code.append(randomLetter);
        }
        return code.toString();
    }
}
