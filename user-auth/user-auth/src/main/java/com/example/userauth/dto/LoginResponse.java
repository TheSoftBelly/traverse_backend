package com.example.userauth.dto;

public class LoginResponse {

    private boolean success;
    private String message;
    private String email;
    private String name;
    private String token;  // 추가된 필드
    private String adminId;  // 추가된 필드
    private String role;  // 추가된 필드

    // 생성자
    public LoginResponse(boolean success, String message, String email, String name, String token, String adminId, String role) {
        this.success = success;
        this.message = message;
        this.email = email;
        this.name = name;
        this.token = token;
        this.adminId = adminId;
        this.role = role;
    }

    // Getter 메서드들
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public String getAdminId() {
        return adminId;
    }

    public String getRole() {
        return role;
    }
}
