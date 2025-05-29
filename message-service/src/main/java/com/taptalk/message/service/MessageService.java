package com.taptalk.message.service;

import com.taptalk.message.model.MessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final KafkaTemplate<String, MessageEvent> kafkaTemplate;
    private static final String TOPIC = "chat.unread";

    public void handleNewMessage(MessageEvent message) {
        try {
            // 1초 대기
            Thread.sleep(1000);
            
            // 메시지 읽음 상태 확인
            if (!message.isRead()) {
                // Kafka에 이벤트 발행
                kafkaTemplate.send(TOPIC, message.getReceiverId(), message);
                log.info("Unread message event published: {}", message);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Error while handling message: {}", e.getMessage());
        }
    }
} 