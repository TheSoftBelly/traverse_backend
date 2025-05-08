package com.taptalk.aimap.service;

import com.taptalk.aimap.dto.auth.*;
import com.taptalk.aimap.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 인증 관련 비즈니스 로직을 처리하는 서비스 인터페이스
 */
@Service
public interface AuthService {
    void sendVerificationEmail(String email);
    boolean verifyEmailCode(String email, String code);
    void setPassword(SetPasswordRequest request);
    String login(LoginRequest request);
    User register(RegisterRequest request);
    User registerProfile(Long userId, RegisterProfileRequest request);
    String uploadProfileImage(MultipartFile file);
} 