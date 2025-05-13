package com.example.userauth.dto;

import lombok.Data;

@Data
public class NotificationTemplateResponseDTO {
    private String id;
    private String name;
    private String type;
    private String content;
    private String created_at;
    private String updated_at;
    private Long usage_count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Long getUsage_count() {
        return usage_count;
    }

    public void setUsage_count(Long usage_count) {
        this.usage_count = usage_count;
    }
}
