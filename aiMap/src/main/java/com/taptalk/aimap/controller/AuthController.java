package com.taptalk.aimap.controller;

import com.taptalk.aimap.dto.auth.*;
import com.taptalk.aimap.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/email-verification")
    public ResponseEntity<Void> sendVerificationEmail(@Valid @RequestBody EmailVerificationRequest request) {
        authService.sendVerificationEmail(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-code")
    public ResponseEntity<Boolean> verifyEmailCode(@Valid @RequestBody VerifyCodeRequest request) {
        boolean isValid = authService.verifyEmailCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok(isValid);
    }

    @PostMapping("/set-password")
    public ResponseEntity<Void> setPassword(@Valid @RequestBody SetPasswordRequest request) {
        authService.setPassword(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping(value = "/register", consumes = "multipart/form-data")
    public ResponseEntity<Void> register(@Valid RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/register-profile", consumes = "multipart/form-data")
    public ResponseEntity<Void> registerProfile(
            @RequestHeader("Authorization") String token,
            @Valid RegisterProfileRequest request) {
        // TODO: 토큰에서 userId 추출
        Long userId = 1L;
        authService.registerProfile(userId, request);
        return ResponseEntity.ok().build();
    }
} 