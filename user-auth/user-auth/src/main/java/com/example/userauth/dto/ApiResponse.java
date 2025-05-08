package com.example.userauth.dto;

public class ApiResponse {
    private boolean success;
    private String message;
    private Object data;  // 선택적 데이터 필드

    // boolean과 String만 받는 생성자
    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // boolean, String, 그리고 data를 받는 생성자 (선택적)
    public ApiResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // Getters와 Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
