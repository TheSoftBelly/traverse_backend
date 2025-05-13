package com.example.userauth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NotificationHistoryResponseDTO {
    private boolean success;
    private DataWrapper data;

    @Data
    @AllArgsConstructor
    public static class DataWrapper {
        private List<NotificationDto> notifications;
        private long total_count;
        private int current_page;
        private int total_pages;
    }

    @Data
    @AllArgsConstructor
    public static class NotificationDto {
        private String id;
        private String template_id;
        private String template_name;
        private String type;
        private String sent_at;
        private int recipient_count;
        private int read_count;
        private String sent_by;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public DataWrapper getData() {
        return data;
    }

    public void setData(DataWrapper data) {
        this.data = data;
    }
}