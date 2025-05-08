package com.example.userauth.model;

import jakarta.persistence.*;

@Entity
@Table(name = "admin")  // 테이블명 지정
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private boolean emailCertification = true; // 기본값 false 설정
    private String passwordConfirm; // 기본값 빈 문자열 설정
    private String invitecode;  // inviteCode를 authCode로 변경
    private String role;
    // Getters and Setters

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isEmailCertification() {
        return emailCertification;
    }

    public void setEmailCertification(boolean emailCertification) {
        this.emailCertification = emailCertification;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getInvitecode() {
        return invitecode;
    }

    public void setInvitecode(String invitecode) {
        this.invitecode = invitecode;
    }


}
