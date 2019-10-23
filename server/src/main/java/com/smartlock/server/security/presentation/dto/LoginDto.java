package com.smartlock.server.security.presentation.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class LoginDto {

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min=8, max=32)
    @Pattern(regexp = "^[a-zA-Z0-9_.!@#$%^&*()=-]+$")
    private String password;

    public LoginDto() {
    }

    public LoginDto(@NotNull @Email String email, @NotNull @Size(min = 8, max = 32) @Pattern(regexp = "^[a-zA-Z0-9_.!@#$%^&*()=-]+$") String password) {
        this.email = email.toLowerCase();
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
