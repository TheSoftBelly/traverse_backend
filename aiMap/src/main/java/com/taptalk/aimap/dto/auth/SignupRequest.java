package com.taptalk.aimap.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원가입 요청을 위한 DTO
 */
@Getter
@NoArgsConstructor
public class SignupRequest {
    private String email;
    private String password;
    private String name;
} 