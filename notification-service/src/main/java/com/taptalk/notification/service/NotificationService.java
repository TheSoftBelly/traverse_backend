package com.taptalk.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    
    private final UserTokenService userTokenService;
    private final FirebaseMessaging firebaseMessaging;
    
    @KafkaListener(topics = "notifications", groupId = "notification-service")
    public void handleNotification(String message) {
        try {
            // 메시지 파싱 (실제 구현에서는 JSON 파싱 필요)
            String[] parts = message.split(":");
            String userId = parts[0];
            String title = parts[1];
            String body = parts[2];
            
            // FCM 토큰 조회
            String fcmToken = userTokenService.getFcmToken(userId);
            
            // FCM 메시지 생성
            Message fcmMessage = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build())
                .build();
            
            // FCM 메시지 전송
            String response = firebaseMessaging.send(fcmMessage);
            log.info("Successfully sent notification: {}", response);
            
        } catch (Exception e) {
            log.error("Failed to send notification", e);
            throw new RuntimeException("Failed to send notification", e);
        }
    }
} 