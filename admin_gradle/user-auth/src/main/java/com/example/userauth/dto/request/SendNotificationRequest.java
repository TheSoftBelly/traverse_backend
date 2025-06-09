package com.example.userauth.dto.request;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class SendNotificationRequest {
    private String template_id;
    private List<String> recipients;
    private Map<String, String> variables;
} 