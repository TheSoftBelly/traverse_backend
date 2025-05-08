package com.taptalk.aimap.service.impl;

import com.taptalk.aimap.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();

    @Override
    public void sendVerificationEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("이메일 인증 코드");
        message.setText("인증 코드: " + code);
        
        mailSender.send(message);
        verificationCodes.put(email, code);
    }

    @Override
    public boolean verifyCode(String email, String code) {
        String storedCode = verificationCodes.get(email);
        if (storedCode != null && storedCode.equals(code)) {
            verificationCodes.remove(email);
            return true;
        }
        return false;
    }
} 