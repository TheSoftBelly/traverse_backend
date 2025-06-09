package com.example.userauth.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class NotificationTemplateResponseDTO {
    private String id;
    private String name;
    private String type;
    private String content;
    private String created_at;
    private String updated_at;
    private Long usage_count;
    private List<String> variables;
} 