package com.example.userauth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SendNotificationRequest {
    @NotEmpty
    private List<String> recipients;
    @NotBlank
    private String template_id;
    private Map<String, String> variables;
    private boolean send_immediately;

    public @NotEmpty List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(@NotEmpty List<String> recipients) {
        this.recipients = recipients;
    }

    public @NotBlank String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(@NotBlank String template_id) {
        this.template_id = template_id;
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = variables;
    }

    public boolean isSend_immediately() {
        return send_immediately;
    }

    public void setSend_immediately(boolean send_immediately) {
        this.send_immediately = send_immediately;
    }
}
