package com.example.userauth.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class NotificationTemplateRequest {
    private String id;
    private String name;
    private String type;
    private String content;
    private List<String> variables;
} 