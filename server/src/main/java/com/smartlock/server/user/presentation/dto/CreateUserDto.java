package com.smartlock.server.user.presentation.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CreateUserDto {

    @NotNull
    @Size(min=8, max=32)
    @Pattern(regexp = "^[a-zA-Z0-9_.!@#$%^&*()=-]+$")
    private String password;

    @NotNull
    @Email
    private String email;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }
}
