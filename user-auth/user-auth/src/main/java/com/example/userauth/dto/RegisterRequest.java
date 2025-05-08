package com.example.userauth.dto;

import java.util.Optional;

public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String inviteCode;  //inviteCode인데 일단authCode로 a
    private boolean emailCertification = true;
    private Optional<String> passwordConfirm = Optional.empty(); // Optional로 처리

     public boolean isEmailCertification() {
        return emailCertification;
    }

    public void setEmailCertification(boolean emailCertification) {
        this.emailCertification = emailCertification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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



    public Optional<String> getPasswordConfirm() {
       return passwordConfirm;
    }

    public void setPasswordConfirm(Optional<String> passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    // Getters and Setters
}

