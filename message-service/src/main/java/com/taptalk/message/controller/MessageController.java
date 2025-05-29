package com.taptalk.message.controller;

import com.taptalk.message.model.MessageEvent;
import com.taptalk.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/check")
    public ResponseEntity<Void> checkMessageStatus(@RequestBody MessageEvent message) {
        messageService.handleNewMessage(message);
        return ResponseEntity.ok().build();
    }
} 