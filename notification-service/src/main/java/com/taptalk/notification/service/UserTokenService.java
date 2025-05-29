package com.taptalk.notification.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserTokenService {
    
    private final FirebaseAuth firebaseAuth;
    
    public String getFcmToken(String userId) {
        try {
            UserRecord userRecord = firebaseAuth.getUser(userId);
            return userRecord.getCustomClaims().get("fcmToken").toString();
        } catch (FirebaseAuthException e) {
            log.error("Failed to get FCM token for user: {}", userId, e);
            throw new RuntimeException("Failed to get FCM token", e);
        }
    }
    
    public void saveFcmToken(String userId, String fcmToken) {
        try {
            UserRecord userRecord = firebaseAuth.getUser(userId);
            firebaseAuth.setCustomUserClaims(userId, 
                java.util.Map.of("fcmToken", fcmToken));
            log.info("FCM token saved for user: {}", userId);
        } catch (FirebaseAuthException e) {
            log.error("Failed to save FCM token for user: {}", userId, e);
            throw new RuntimeException("Failed to save FCM token", e);
        }
    }
} 