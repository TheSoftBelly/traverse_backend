package com.example.userauth.controller;

import com.example.userauth.dto.NotificationHistoryResponseDTO;
import com.example.userauth.dto.NotificationTemplateRequest;
import com.example.userauth.dto.SendNotificationRequest;
import com.example.userauth.service.NotificationHistoryService;
import com.example.userauth.service.NotificationTemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationTemplateController {

    private final NotificationTemplateService templateService;
    private final NotificationHistoryService historyService;

    // 알림 템플릿 목록 조회 API
    @GetMapping("/templates")
    public ResponseEntity<?> getTemplates(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "all") String type
    ) {
        try {
            Map<String, Object> response = templateService.getTemplates(page, limit, type);
            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "error", "서버 오류: " + e.getMessage()));
        }
    }

    // 알림 템플릿 생성 API
    @PostMapping("/templates")
    public ResponseEntity<Object> createTemplate(@RequestBody NotificationTemplateRequest request) {
        try {
            String id = templateService.createTemplate(request);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "알림 템플릿이 저장되었습니다",
                    "data", Map.of("id", id)
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }

    // 알림 템플릿 수정 API
    @PutMapping("/templates")
    public ResponseEntity<Object> updateTemplate(@RequestBody NotificationTemplateRequest request) {
        try {
            String id = templateService.updateTemplate(request);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "알림 템플릿이 저장되었습니다",
                    "data", Map.of("id", id)
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        }
    }

    // 알림 발송 API
    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendNotification(@RequestBody @Valid SendNotificationRequest request) throws Exception {
        return ResponseEntity.ok(templateService.sendNotification(request));
    }

    // 알림 발송 내역 조회 API
    @GetMapping("/history")
    public NotificationHistoryResponseDTO getHistory(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String start_date,
            @RequestParam(required = false) String end_date
    ) throws ExecutionException, InterruptedException {
        return historyService.getNotificationHistory(type, start_date, end_date, page, limit);
    }
}
