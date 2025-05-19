package com.example.userauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InviteCodeValidationResponse {
    @JsonProperty("success")
    private boolean success;

    @JsonProperty("data")
    private Data data;

    @JsonProperty("message")
    private String message;

    public InviteCodeValidationResponse(boolean success, Data data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    @lombok.Data
    public static class Data {
        @JsonProperty("is_valid")
        private boolean isValid;

        @JsonProperty("role")
        private String role;

        public Data(boolean isValid, String role) {
            this.isValid = isValid;
            this.role = role;
        }

        public boolean isValid() {
            return isValid;
        }

        public void setValid(boolean valid) {
            isValid = valid;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}
