package com.taptalk.aimap.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그인 요청을 위한 DTO
 */
@Getter
@NoArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
} 