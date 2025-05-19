package com.taptalk.aimap.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendVerificationEmail(String email, String code);
    boolean verifyCode(String email, String code);
} 